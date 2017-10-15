package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import java.util.Map;

import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Transformer;

import com.hazelcast.jet.stream.IStreamList;

public class KMeansModel implements Transformer<String,Integer>{

	@Override
	public IStreamList<Map<String, Integer>> transform(IStreamList<Map<String, Integer>> dataset) {
		// TODO Add assigned cluster to the row (map)
		return dataset;
	}

}
