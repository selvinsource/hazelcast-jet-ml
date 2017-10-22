package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.selvinsource.hazelcast_jet_ml.ml.clustering.KMeans;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.IStreamList;

public class PipelineTest {
	
	JetInstance instance1 = Jet.newJetInstance();
	IStreamList<double[]> trainDataset;
	IStreamList<double[]> testDataset;
	
	@Before
	public void setup(){
		trainDataset = instance1.getList("trainDataset");
		trainDataset.add(new double[] {15});
		trainDataset.add(new double[] {15});
		trainDataset.add(new double[] {16});
		trainDataset.add(new double[] {19});
		trainDataset.add(new double[] {19});
		trainDataset.add(new double[] {20});
		trainDataset.add(new double[] {20});
		trainDataset.add(new double[] {21});
		trainDataset.add(new double[] {22});
		trainDataset.add(new double[] {28});
		trainDataset.add(new double[] {35});
		trainDataset.add(new double[] {40});
		trainDataset.add(new double[] {41});
		trainDataset.add(new double[] {42});
		trainDataset.add(new double[] {43});
		trainDataset.add(new double[] {44});
		trainDataset.add(new double[] {60});
		trainDataset.add(new double[] {61});
		trainDataset.add(new double[] {65});
		testDataset = instance1.getList("testDataset");
		testDataset.add(new double[] {1});
		testDataset.add(new double[] {100});
	}	
	
	@Test
    public void testPipeline()
    {
		// Arrange
		int k = 2;
		int maxIter = 20;
		List<double[]> initialCentroids = new ArrayList<double[]>();
		initialCentroids.add(new double[] {16});
		initialCentroids.add(new double[] {22});	
		Estimator<double[]> estimator = new KMeans(k, maxIter, initialCentroids);

		// Act
		// Hazelcast Get ML Pipeline
		List<double[]> outputDataset1 = estimator.fit(trainDataset).transform(testDataset);
								
		// Assert
		// Check output predictions (first instance assigned to first cluster, second instance assigned to second cluster)
        assertTrue( outputDataset1.get(0)[1] == 0);
        assertTrue( outputDataset1.get(1)[1] == 1);
        // Check the transform generates a distributed Hazelcast IList called kMeansOutputDataset
        List<double[]> kMeansOutputDataset = instance1.getList("kMeansOutputDataset");
        assertTrue( kMeansOutputDataset.size() == 2);
    }

}
