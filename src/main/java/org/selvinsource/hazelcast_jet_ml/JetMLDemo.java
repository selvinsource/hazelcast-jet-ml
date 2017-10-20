package org.selvinsource.hazelcast_jet_ml;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.selvinsource.hazelcast_jet_ml.ml.clustering.KMeans;
import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Estimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetMLDemo 
{

	private static final Logger LOGGER = LoggerFactory.getLogger(JetMLDemo.class);
	
    public enum EstimatorType {
        KMeans;
    }

    public static void main( String[] args )
    {
        if(args.length != 1){
            System.out.println("Please select an estimator: KMeans");
            return;
        }
        switch(EstimatorType.valueOf(args[0])) {
        case KMeans:
            System.out.println(EstimatorType.KMeans + " selected");
            runKMeansJetMLPipelineDemo();
            break;
        default:
            System.out.println("Estimator selected not implemented");
            return;
        }
    }

    private static void runKMeansJetMLPipelineDemo() {
    	// Read the iris.csv file into a list of double[]
    	// Note: skip(1) removes the header, limit(attributes.length-1) removes the class column
    	List<double[]> inputDataset = getFileContent("/datasets/iris.csv").skip(1).map(line -> {
    		String[] attributes = line.split(",");
    		// Convert the attributes from string to double, excluding the last one which is not numeric and it is the unsupervised cluster the algorithm is going to form
    		return Arrays.stream(attributes).limit(attributes.length-1).mapToDouble(Double::parseDouble).toArray();
    		}
    	).collect(Collectors.toList());
    	
    	// Create a KMeans estimator with k = 3 (ideally it should match the known class: Iris-setosa, Iris-versicolor, Iris-virginica)
		Estimator<double[]> estimator = new KMeans(3, 20);
		// Hazelcast Get ML Pipeline
		// Train using inputDataset and test using the same inputDataset to see if the predicted classes are comparable to the known ones
		List<double[]> outputDataset = estimator.fit(inputDataset).transform(inputDataset);    	
		outputDataset.stream().forEach(i -> LOGGER.info(Arrays.toString(i)));
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
