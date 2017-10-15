package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.util.Map;

import com.hazelcast.jet.stream.IStreamList;

/**
 * Transform one dataset into another
 * the dataset (a table) is represented by a distributed IList where each row is a map of column name and value
 */
public interface Transformer<K,V> {

	IStreamList<Map<K,V>> transform(IStreamList<Map<K,V>> dataset);
	
}
