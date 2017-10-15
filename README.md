## Hazelcast JET ML

Collection of machine learning algorithms using the distributed computing platform Hazelcast JET.

Use JetMLDemo as example of usage of the ML Pipeline.

## Installation
```sh
git clone https://github.com/selvinsource/hazelcast-jet-ml.git
cd hazelcast-jet-ml
mvn clean compile assembly:single
```

## Documentation
ML Pipeline: desc here.

## Datasets
The following datasets have been used:
* [Iris]

## K-Means Clustering
```sh
java -jar target/hazelcast-jet-ml-1.0-SNAPSHOT-jar-with-dependencies.jar KMeans
```

[Iris]:https://github.com/selvinsource/hazelcast-jet-ml/blob/master/src/main/resources/datasets/iris.csv