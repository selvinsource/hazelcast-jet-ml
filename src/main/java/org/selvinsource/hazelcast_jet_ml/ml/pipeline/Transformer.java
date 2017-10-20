package org.selvinsource.hazelcast_jet_ml.ml.pipeline;

import java.util.List;

/**
 * Transform an input dataset into an output dataset (for example by adding an additional prediction attribute)
 */
public interface Transformer<T> {

	List<T> transform(List<T> dataset);
	
}
