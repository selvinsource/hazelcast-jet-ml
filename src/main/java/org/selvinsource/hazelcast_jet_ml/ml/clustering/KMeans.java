package org.selvinsource.hazelcast_jet_ml.ml.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.selvinsource.hazelcast_jet_ml.ml.pipeline.Estimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMeans implements Estimator<double[]>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KMeans.class);

	private int k = 2;
	private int maxIter = 20;
	private List<double[]> initialCentroids;
	
	/**
	 * Clustering is the task of identifying and segmenting the instances into a finite number (k) of categories (clusters) which are not predefined (unlike classification).
	 * K-Means is the classic clustering technique that partitions the instances into k clusters whereas k is predefined. 
	 * k = 2 the number of desired clusters
	 * maxIter = 20 maximum number of iteration if not converging before the max
	 */
	public KMeans(){
	}	
	
	/**
	 * Clustering is the task of identifying and segmenting the instances into a finite number (k) of categories (clusters) which are not predefined (unlike classification).
	 * K-Means is the classic clustering technique that partitions the instances into k clusters whereas k is predefined. 
	 * @param k the number of desired clusters
	 * @param maxIter maximum number of iteration if not converging before the max
	 */
	public KMeans(int k, int maxIter){
		this.k = k;
		this.maxIter = maxIter;
	}
	
	/**
	 * Clustering is the task of identifying and segmenting the instances into a finite number (k) of categories (clusters) which are not predefined (unlike classification).
	 * K-Means is the classic clustering technique that partitions the instances into k clusters whereas k is predefined. 
	 * @param k the number of desired clusters
	 * @param maxIter maximum number of iteration if not converging before the max
	 * @param initialCentroids provide initial centroids to start with in case the random selection is not ideal (results are sensitive to initialization)
	 */	
	public KMeans(int k, int maxIter, List<double[]> initialCentroids){
		this.k = k;
		this.maxIter = maxIter;
		this.initialCentroids = initialCentroids;
	}	
	
	@Override
	public KMeansModel fit(List<double[]> dataset) {
		if(dataset.size()==0)
			throw new RuntimeException("Input dataset cannot be empty for KMeans clusting algorithm.");
		
		List<double[]> newCentroids = initialCentroids!=null ? initialCentroids : selectRandomCentroids(this.k, dataset);
		
		List<double[]> currentCentroids;
		int iter = 0;
		do{
			iter++;
			currentCentroids = newCentroids;
			newCentroids = runKMeansAlgorithm(dataset, currentCentroids);
		}while(!equalsCentroids(newCentroids, currentCentroids) && iter < maxIter);
		
		return new KMeansModel(newCentroids);
	}
	
	private static List<double[]> selectRandomCentroids(int k, List<double[]> dataset){
			Random r = new Random();
		// Select k points at random as cluster centroids, however if the input dataset size is less than the desired clusters, use the input size		
		Set<Integer> randomIndexes = new HashSet<>();
		while(randomIndexes.size() < Math.min(k, dataset.size())){
			randomIndexes.add(r.nextInt(dataset.size()));
		}
		return randomIndexes.stream().map(dataset::get).collect(Collectors.toList());
	}
	
	private static List<double[]> runKMeansAlgorithm(List<double[]> dataset, List<double[]> centroids){
		// Get number of attributes from the first input dataset instance
		int inputSize = dataset.get(0).length;		
		// Create a list of new centroids initialized to zeros
		List<double[]> newCentroids = new ArrayList<double[]>();
		centroids.forEach(
			i-> newCentroids.add(new double[inputSize])
		);
		int[] newCentroidsSizes = new int[centroids.size()];
		// Assign objects to their closest cluster center according to the Euclidean distance function
		dataset.stream().forEach(
			i -> {
				int closestCentroidIndex = findClosestCentroid(i, centroids);
				Arrays.setAll(newCentroids.get(closestCentroidIndex), el -> newCentroids.get(closestCentroidIndex)[el] + i[el]);
				newCentroidsSizes[closestCentroidIndex]++;
			}
		);
		// Calculate the centroid or mean of all objects in each cluster
		AtomicInteger index = new AtomicInteger();
		LOGGER.info("Clusters:");
		newCentroids.forEach(
			c -> {
				Arrays.setAll(c, i -> c[i] / newCentroidsSizes[index.getAndIncrement()]);
				LOGGER.info(Arrays.toString(c));
			}
		);
		return newCentroids;		
	}	
	
	public static int findClosestCentroid(double[] instance, List<double[]> centroids){
		return centroids.indexOf(
				centroids.stream().reduce(
					(a,b)-> calculateEuclideanDistance(a,instance) < calculateEuclideanDistance(b,instance) ? a:b
				).get()
		);		
	}
	
	private static double calculateEuclideanDistance(double[] array1, double[] array2)
	{
		double sum = 0.0;
		for (int i=0;i<array1.length;i++) {
			sum+=Math.pow((array1[i]-array2[i]),2.0);
		}
		return Math.sqrt(sum);
	}
	
	private static boolean equalsCentroids(List<double[]> centroid1, List<double[]> centroid2){
		// For all centroids in a, find at least one matching in controids b
		return centroid1.stream().allMatch(c1 -> centroid2.stream().anyMatch(c2 -> Arrays.equals(c1, c2)));
	}

}
