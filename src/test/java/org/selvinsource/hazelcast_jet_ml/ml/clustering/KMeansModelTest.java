package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.jet.IListJet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Transformer;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

public class KMeansModelTest {
	
	JetInstance instance1;
	IListJet<double[]> inputDataset1;
	List<double[]> centroids1;
	
	@Before
	public void setup(){
		instance1 = Jet.newJetInstance();
		inputDataset1 = instance1.getList("inputDataset1");
		inputDataset1.add(new double[] {5});
		inputDataset1.add(new double[] {25});
		centroids1 = new ArrayList<double[]>();
		centroids1.add(new double[] {10});
		centroids1.add(new double[] {20});	
	}	
	
	@Test
    public void testTransformPredictionAsIList()
    {
		// Arrange
		KMeansModel model = new KMeansModel(centroids1);

		// Act
		List<double[]> outputDataset1 = model.transform(inputDataset1);
								
		// Assert
		// Check output predictions (first instance assigned to first cluster, second instance assigned to second cluster)
        assertTrue( outputDataset1.get(0)[1] == 0);
        assertTrue( outputDataset1.get(1)[1] == 1);
        // Check the transform generates a distributed Hazelcast IList called kMeansOutputDataset
        List<double[]> kMeansOutputDataset = instance1.getList("kMeansOutputDataset");
        assertTrue( kMeansOutputDataset.size() == 2);
    }		
	
	@Test(expected = RuntimeException.class)
	public void testTransformRuntimeExceptionNoCentroids(){
		// Arrange
		Transformer<double[]> model = new KMeansModel(new ArrayList<double[]>());
		
		// Act
		model.transform(inputDataset1);
	}	
	
	@Test(expected = RuntimeException.class)
	public void testTransformRuntimeExceptionEmptyInput(){
		// Arrange
		Transformer<double[]> model = new KMeansModel(centroids1);
		
		// Act
		model.transform(instance1.getList("emptyDataset1"));
	}
	
	@After
	public void close(){
		instance1.shutdown();
	}	

}
