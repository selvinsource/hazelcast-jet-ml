package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.IStreamList;

public class KMeansTest {
	
	JetInstance jetInstance1;
	IStreamList<double[]> inputDataset1;
	
	@Before
	public void setup(){
		jetInstance1 = Jet.newJetInstance();
		inputDataset1 = jetInstance1.getList("inputDataset1");
		inputDataset1.add(new double[] {15});
		inputDataset1.add(new double[] {15});
		inputDataset1.add(new double[] {16});
		inputDataset1.add(new double[] {19});
		inputDataset1.add(new double[] {19});
		inputDataset1.add(new double[] {20});
		inputDataset1.add(new double[] {20});
		inputDataset1.add(new double[] {21});
		inputDataset1.add(new double[] {22});
		inputDataset1.add(new double[] {28});
		inputDataset1.add(new double[] {35});
		inputDataset1.add(new double[] {40});
		inputDataset1.add(new double[] {41});
		inputDataset1.add(new double[] {42});
		inputDataset1.add(new double[] {43});
		inputDataset1.add(new double[] {44});
		inputDataset1.add(new double[] {60});
		inputDataset1.add(new double[] {61});
		inputDataset1.add(new double[] {65});
	}
	
	@Test
    public void testFindClosestCentroid()
    {
		// Arrange
		double[] instance1 = new double[] {15};
		double[] instance2 = new double[] {65};
		List<double[]> centroids = new ArrayList<double[]>();
		centroids.add(new double[] {19.5});
		centroids.add(new double[] {47.888888888888886});
		
		// Act
		int prediction1 = KMeans.findClosestCentroid(instance1, centroids);
		int prediction2 = KMeans.findClosestCentroid(instance2, centroids);
								
		// Assert
		// Check instance1 is closer to the first centroid
        assertTrue( prediction1 == 0 );
		// Check instance2 is closer to the second centroid        
        assertTrue( prediction2 == 1 );
    }	
	
	@Test
    public void testFitReturnKClusters()
    {
		// Arrange
		int k = 2;
		int maxIter = 20;
		KMeans kMeans = new KMeans(k, maxIter);
		
		// Act
		KMeansModel model = kMeans.fit(inputDataset1);
								
		// Assert
		// Check number of clusters matches k (the desired ones)
        assertTrue( model.getCentroids().size() == 2 );
    }		
	
	@Test
    public void testFitReturnLessThanKClusters()
    {
		// Arrange
		// Requesting more clusters than the input size
		int k = inputDataset1.size() + 5;
		int maxIter = 20;
		KMeans kMeans = new KMeans(k, maxIter);
		
		// Act
		KMeansModel model = kMeans.fit(inputDataset1);
								
		// Assert
		// Check number of clusters matches the input size as k is higher than the input size
        assertTrue( model.getCentroids().size() == inputDataset1.size() );
    }
	
	@Test
    public void testFitReturnedCentroidsWithProvidedInitialCentroids()
    {
		// Arrange
		int k = 2;
		int maxIter = 20;
		List<double[]> initialCentroids = new ArrayList<double[]>();
		initialCentroids.add(new double[] {16});
		initialCentroids.add(new double[] {22});		
		KMeans kMeans = new KMeans(k, maxIter, initialCentroids);

		// Act
		KMeansModel model = kMeans.fit(inputDataset1);
								
		// Assert
		// Check returned centroids are always the same given that the initial centroids are provided
        assertTrue( model.getCentroids().get(0)[0] == 19.5);
        assertTrue( model.getCentroids().get(1)[0] == 47.888888888888886 );
    }		
	
	@Test
    public void testFitReturnedCentroidsWithProvidedInitialCentroidsAndLessIterations()
    {
		// Arrange
		int k = 3;
		int maxIter = 2;
		List<double[]> initialCentroids = new ArrayList<double[]>();
		initialCentroids.add(new double[] {16});
		initialCentroids.add(new double[] {22});		
		KMeans kMeans = new KMeans(k, maxIter, initialCentroids);

		// Act
		KMeansModel model = kMeans.fit(inputDataset1);
								
		// Assert
		// Check returned centroids are always the same given that the initial centroids are provided and only 2 iterations
        assertTrue( model.getCentroids().get(0)[0] == 18.555555555555557);
        assertTrue( model.getCentroids().get(1)[0] == 45.9 );
    }		
	
	@Test(expected = RuntimeException.class)
	public void testFitRuntimeException(){
		// Arrange
		KMeans kMeans = new KMeans();
		
		// Act
		kMeans.fit(jetInstance1.getList("emptyDataset1"));
	}
	
	@After
	public void close(){
		jetInstance1.shutdown();
	}	
	
}
