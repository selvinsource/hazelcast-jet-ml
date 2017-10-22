package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.io.Serializable;

import com.hazelcast.jet.stream.IStreamList;

/**
 * An algorithm to fit an input dataset into a Transformer (for example a model)
 */
public interface Estimator<T> extends Serializable{

	/**
	 * An algorithm to fit an input dataset into a Transformer (for example a model)
	 */
	Transformer<T> fit(IStreamList<T> dataset);
	
}
