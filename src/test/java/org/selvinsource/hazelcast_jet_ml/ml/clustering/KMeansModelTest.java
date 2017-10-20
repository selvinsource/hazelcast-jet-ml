package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class KMeansModelTest {
	
	List<double[]> inputDataset1;
	
	@Before
	public void setup(){
		inputDataset1 = new ArrayList<double[]>();
		inputDataset1.add(new double[] {5});
		inputDataset1.add(new double[] {25});
	}	
	
	@Test
    public void testTransformPrediction()
    {
		// Arrange
		List<double[]> centroids = new ArrayList<double[]>();
		centroids.add(new double[] {10});
		centroids.add(new double[] {20});		
		KMeansModel model = new KMeansModel(centroids);

		// Act
		List<double[]> outputDataset1 = model.transform(inputDataset1);
								
		// Assert
		// Check output predictions (first instance assigned to first cluster, second instance assigend to second cluster)
        assertTrue( outputDataset1.get(0)[1] == 0);
        assertTrue( outputDataset1.get(1)[1] == 1);
    }		
	
	@Test(expected = RuntimeException.class)
	public void testTransformRuntimeException(){
		// Arrange
		KMeansModel model = new KMeansModel(new ArrayList<double[]>());
		
		// Act
		model.transform(new ArrayList<double[]>());
	}	

}
