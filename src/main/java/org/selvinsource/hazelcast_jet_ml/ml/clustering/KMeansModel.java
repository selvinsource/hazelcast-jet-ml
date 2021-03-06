package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import com.hazelcast.jet.IListJet;
import com.hazelcast.jet.stream.DistributedCollectors;
import com.hazelcast.jet.stream.DistributedStream;
import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Transformer;

import java.util.Arrays;
import java.util.List;

public class KMeansModel implements Transformer<double[]>{

	/**
	 * Version 1
	 */
	private static final long serialVersionUID = 1L;
	
	private List<double[]> centroids;
	
	/**
	 * Cluster centers
	 * @param centroids
	 */
	public KMeansModel(List<double[]> centroids){
		this.centroids = centroids;
	}
	
	/**
	 * It creates and returns a distributed Hazelcast IList called kMeansOutputDataset by adding the predicted cluster as additional attribute to the input dataset
	 */
	@Override
	public IListJet<double[]> transform(IListJet<double[]> dataset) {
		if(centroids.size()==0)
			throw new RuntimeException("Centroids cannot be empty for KMeansModel.");
		if(dataset.size()==0)
			throw new RuntimeException("Dataset to transform cannot be empty for KMeansModel.");
		
		return DistributedStream.fromList(dataset).map(i -> {
			double[] t = Arrays.copyOf(i, i.length + 1);
		    t[i.length] = KMeans.findClosestCentroid(i, centroids);
		    return t;
		}).collect(DistributedCollectors.toIList("kMeansOutputDataset"));
	}

	public List<double[]> getCentroids() {
		return centroids;
	}

}
