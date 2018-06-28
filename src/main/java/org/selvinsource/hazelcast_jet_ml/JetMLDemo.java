package org.selvinsource.hazelcast_jet_ml;

import com.hazelcast.jet.IListJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.DistributedCollectors;
import com.hazelcast.jet.stream.DistributedStream;
import org.selvinsource.hazelcast_jet_ml.ml.clustering.KMeans;
import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Estimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JetMLDemo 
{

	private static final Logger LOGGER = LoggerFactory.getLogger(JetMLDemo.class);
	
    public enum EstimatorType {
        KMeans;
    }

    public static void main( String[] args )
    {
        if(args.length != 1){
			LOGGER.info("Please select an estimator: KMeans");
            return;
        }
        switch(EstimatorType.valueOf(args[0])) {
        case KMeans:
			LOGGER.info(EstimatorType.KMeans + " selected");
            runKMeansJetMLPipelineDemo();
            break;
        default:
			LOGGER.info("Estimator selected not implemented");
            return;
        }
    }

    private static void runKMeansJetMLPipelineDemo() {
    	try {
    		// Create two Jet members
    		JetInstance instance1 = Jet.newJetInstance();
    		Jet.newJetInstance();
    		
    		// Ultimately read the iris.csv file into an Hazelcast IListJet<double[]> (which is not distributed)
    		// First load the whole file including the last column (the class) which is not needed to fit the model but useful to compare the predicted class with the real class
    		// Note: skip(1) removes the header
    		IListJet<String[]> inputDatasetWithClass = instance1.getList("inputDatasetWithClass");
    		getFileContent("/datasets/iris.csv").skip(1).forEach(
    				line -> inputDatasetWithClass.add(line.split(","))
    		);
    		// Note: convert attributes into doubles, limit(attributes.length-1) removes the class column not needed for clustering as it is an unsupervised algorithm
			IListJet<double[]> inputDataset = DistributedStream.fromList(inputDatasetWithClass).map(
    				attr -> Arrays.stream(attr).limit(attr.length-1).mapToDouble(Double::parseDouble).toArray()
    		).collect(DistributedCollectors.toIList("inputDataset"));

    		// Create a KMeans estimator with k = 3 (ideally it should match the known class: Iris-setosa, Iris-versicolor, Iris-virginica)
    		Estimator<double[]> estimator = new KMeans(3, 20);
    		// Hazelcast Jet ML Pipeline
    		// Train using inputDataset and test using the same inputDataset to see if the predicted classes are comparable to the known ones
			IListJet<double[]> outputDataset = estimator.fit(inputDataset).transform(inputDataset);

    		// Print results comparing predicted (last double which is 0, 1 or 2) and the known class
    		LOGGER.info("Print output with predicted cluster and original known class:");
    		AtomicInteger index = new AtomicInteger();
			DistributedStream.fromList(outputDataset).forEach(
    				i -> LOGGER.info(Arrays.toString(i) + " known as " + inputDatasetWithClass.get(index.getAndIncrement())[4])
    		);

    		// Note: as the maps below are as small as the clusters (k=3), no need to use DistributedCollectors.groupingByToIMap
    		LOGGER.info("Summary of original class:");
    		Map<String, Long> summaryOriginal = DistributedStream.fromList(inputDatasetWithClass).collect(
    				DistributedCollectors.groupingBy(i -> i[4], DistributedCollectors.counting())
    		);
    		LOGGER.info(summaryOriginal.toString());

    		LOGGER.info("Summary of clustered instances:");
    		Map<Double, Long> summaryClustered = DistributedStream.fromList(outputDataset).collect(
    				DistributedCollectors.groupingBy(i -> i[4], DistributedCollectors.counting())
    		);
    		LOGGER.info(summaryClustered.toString());

    		// Some comparison to another machine learning library
    		LOGGER.info("Computed clusters are comparable to the one generated by Spark MLLib given the same input file: https://github.com/selvinsource/spark-pmml-exporter-validator/blob/master/src/main/resources/exported_pmml_models/kmeans.xml");
    	} finally {
    		Jet.shutdownAll();
    	}
    }
    
    private static Stream<String> getFileContent(String fileName) {
    	final BufferedReader r = new BufferedReader(new InputStreamReader(JetMLDemo.class.getResourceAsStream(fileName), UTF_8));
    	return r.lines().onClose(() -> close(r));
    }    

    private static void close(Closeable c) {
    	try {
    		c.close();
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }    

}
