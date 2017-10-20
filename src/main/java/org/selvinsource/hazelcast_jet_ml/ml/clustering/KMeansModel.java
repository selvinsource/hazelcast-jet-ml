package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Transformer;

public class KMeansModel implements Transformer<double[]>{

	private List<double[]> centroids;
	
	/**
	 * Cluster centers
	 * @param centroids
	 */
	public KMeansModel(List<double[]> centroids){
		this.centroids = centroids;
	}
	
	@Override
	public List<double[]> transform(List<double[]> dataset) {
		if(dataset.size()==0)
			throw new RuntimeException("Dataset to transform cannot be empty for KMeansModel.");
		
		return dataset.stream().map(i -> {
			double[] t = Arrays.copyOf(i, i.length + 1);
		    t[i.length] = KMeans.findClosestCentroid(i, centroids);
		    return t;
		}).collect(Collectors.toList());
	}

	public List<double[]> getCentroids() {
		return centroids;
	}

}
