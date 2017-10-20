## Hazelcast JET ML

Machine learning algorithms using the distributed computing platform Hazelcast JET.

Use JetMLDemo as example of usage of the Jet ML Pipeline.

## Installation
```sh
git clone https://github.com/selvinsource/hazelcast-jet-ml.git
cd hazelcast-jet-ml
mvn clean compile assembly:single
```

## Documentation
The Jet ML Pipeline allows to chain Estimators and Transformers.

* The Estimator is an algorithm that returns a Transformer given a dataset (IStreamList<Map<K,V>>) to fit
* The Transformer is an ML model that transforms one dataset (IStreamList<Map<K,V>>) into another

Inspired by scikit-learn, see [paper].

## Datasets
The following datasets have been used:
* [Iris]

## K-Means Clustering
```sh
java -jar target/hazelcast-jet-ml-1.0-SNAPSHOT-jar-with-dependencies.jar KMeans
```

[Iris]:https://github.com/selvinsource/hazelcast-jet-ml/blob/master/src/main/resources/datasets/iris.csv
[paper]:https://arxiv.org/abs/1309.0238