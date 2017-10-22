## Hazelcast JET ML

Machine learning algorithms using the distributed computing platform Hazelcast JET.

Use JetMLDemo as example of usage of the Jet ML Pipeline.

## Installation
```sh
git clone https://github.com/selvinsource/hazelcast-jet-ml.git
cd hazelcast-jet-ml
mvn clean compile test assembly:single
```

## Documentation
The Jet ML Pipeline allows to chain Estimators and Transformers.

* The Estimator is an algorithm that returns a Transformer given a dataset to fit
* The Transformer is an ML model that transforms one dataset into another
* A dataset is represented by a distributed Hazelcast IStreamList<T>

Inspired by scikit-learn, see [paper].

## Datasets
The following datasets have been used:
* [Iris]

## K-Means Clustering Examples
Train a model and show identified clusters 
```java
// Create two Jet members
JetInstance instance1 = Jet.newJetInstance();
Jet.newJetInstance();

// Get a distributed training dataset (it is assumed this is already populated, e.g. from a file)
IStreamList<double[]> trainDataset = instance1.getList("trainDataset"); 

// Train a model using the train dataset, k = 3, maxIter = 20
// k = 3 the number of desired clusters
// maxIter = 20 maximum number of iteration if not converging
KMeans kMeans = new KMeans(3, 20);
KMeansModel model = kMeans.fit(trainDataset);

// Show the identified centroids
LOGGER.info("Centroids:");
model.getCentroids().stream().forEach(c -> LOGGER.info(Arrays.toString(c)));

Jet.shutdownAll();
```

Train a model and predict test data using Jet ML Pipeline
```java
// Create two Jet members
JetInstance instance1 = Jet.newJetInstance();
Jet.newJetInstance();
 
// Get a distributed datasets to train the model and then test it
IStreamList<double[]> trainDataset = instance1.getList("trainDataset"); 
IStreamList<double[]> testDataset = instance1.getList("testDataset"); 

// Create a KMeans estimator
Estimator<double[]> estimator = new KMeans(3, 20);

// Hazelcast Get ML Pipeline: given a train dataset the estimator (KMeans) returns a transformer (KMeanModel) which assigns clusters to test dataset instances
IStreamList<double[]> outputDataset = estimator.fit(trainDataset).transform(testDataset);

Jet.shutdownAll();
```

## K-Means Clustering Demo
```sh
java -jar target/hazelcast-jet-ml-1.0-SNAPSHOT-jar-with-dependencies.jar KMeans
```
See [full code].

[Iris]:https://github.com/selvinsource/hazelcast-jet-ml/blob/master/src/main/resources/datasets/iris.csv
[paper]:https://arxiv.org/abs/1309.0238
[full code]:https://github.com/selvinsource/hazelcast-jet-ml/blob/master/src/main/java/org/selvinsource/hazelcast_jet_ml/JetMLDemo.java