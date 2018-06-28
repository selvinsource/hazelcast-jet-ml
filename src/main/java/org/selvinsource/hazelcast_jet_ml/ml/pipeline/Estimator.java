package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import com.hazelcast.jet.IListJet;

import java.io.Serializable;

/**
 * An algorithm to fit an input dataset into a Transformer (for example a model)
 */
public interface Estimator<T> extends Serializable{

	/**
	 * An algorithm to fit an input dataset into a Transformer (for example a model)
	 */
	Transformer<T> fit(IListJet<T> dataset);

}
