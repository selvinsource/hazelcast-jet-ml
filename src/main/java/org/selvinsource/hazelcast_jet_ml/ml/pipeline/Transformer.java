package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.io.Serializable;

import com.hazelcast.jet.stream.IStreamList;

/**
 * Transform an input dataset into an output dataset (for example by adding an additional prediction attribute)
 */
public interface Transformer<T> extends Serializable{

	/**
	 * Transform an input dataset into an output dataset (for example by adding an additional prediction attribute)
	 */
	IStreamList<T> transform(IStreamList<T> dataset);
	
}
