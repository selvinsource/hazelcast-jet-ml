package org.selvinsource.hazelcast_jet_ml;

import java.util.Map;

import org.selvinsource.hazelcast_jet_ml.ml.clustering.KMeans;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.stream.IStreamList;
import com.hazelcast.jet.stream.IStreamMap;

/**
 * Hello world!
 *
 */
public class JetMLDemo 
{
	
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
        JetInstance jet = Jet.newJetInstance();
        // Create an additional instance; it will automatically
        // discover the first one and form a cluster
        Jet.newJetInstance();
 
        IStreamMap<String, Integer> map = jet.getMap("latitudes");
        map.put("London", 51);
        map.put("Paris", 48);
        map.put("NYC", 40);
        map.put("Sydney", -34);
        map.put("Sao Paulo", -23);
        map.put("Jakarta", -6);
        map.entrySet().stream().filter(e -> e.getValue() < 0).forEach(System.out::println);
        
        IStreamList<Map<String, Integer>> trainingDataset = jet.getList("training");
		
        IStreamList<Map<String, Integer>> testDataset = jet.getList("test");
		
		new KMeans().fit(trainingDataset).transform(testDataset);
        
        Jet.shutdownAll();
	}
	
}
