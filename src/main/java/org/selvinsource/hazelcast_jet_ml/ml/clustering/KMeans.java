package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import java.util.Map;

import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Estimator;
import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Transformer;

import com.hazelcast.jet.stream.IStreamList;

public class KMeans implements Estimator<String,Integer>{

	@Override
	public Transformer<String,Integer> fit(IStreamList<Map<String, Integer>> dataset) {
		// TODO run algorithm and set model centroids
		return new KMeansModel();
	}

}
