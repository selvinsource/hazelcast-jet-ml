package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.util.List;

/**
 * An algorithm to fit an input dataset into a Transformer (for example a model)
 */
public interface Estimator<T> {

	Transformer<T> fit(List<T> dataset);
	
}
