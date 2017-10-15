package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.util.Map;

import com.hazelcast.jet.stream.IStreamList;

/**
 * An algorithm to fit a dataset into a Transformer
 */
public interface Estimator<K,V> {

	Transformer<K,V> fit(IStreamList<Map<K,V>> dataset);
	
}
