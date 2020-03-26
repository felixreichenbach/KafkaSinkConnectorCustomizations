# KafkaSinkConnectorCustomizations
A simple MongoDB Kafka post processor to get you started.


## Prerequisites
Get the Kafka connector demo environment up and running:

http://docs.mongodb.com/kafka-connector/master/kafka-docker-example/

## Add Custom Post Processor To The Build Path
Clone Repo:
https://github.com/felixreichenbach/KafkaSinkConnectorCustomizations

Copy the custom post processor java file to the Kafka demo environment build path:

```./src/main/java/com/mongodb/kafka/connect/sink/processor```

## Deploy Custom Post Processor
When you run the Kafka demo environment it will automatically build the Kafka Connector JAR including the custom post processor.

```./run.sh```

## Configure The Sink Connector

TBD
