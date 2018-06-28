package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import com.hazelcast.jet.IListJet;

import java.io.Serializable;

/**
 * Transform an input dataset into an output dataset (for example by adding an additional prediction attribute)
 */
public interface Transformer<T> extends Serializable{

	/**
	 * Transform an input dataset into an output dataset (for example by adding an additional prediction attribute)
	 */
	IListJet<T> transform(IListJet<T> dataset);
	
}
