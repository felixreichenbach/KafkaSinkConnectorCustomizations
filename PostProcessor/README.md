# How to build a custom PostProcessor
A simple MongoDB Kafka post processor to get you started.


## Prerequisites
Get the Kafka connector demo environment up and running:

http://docs.mongodb.com/kafka-connector/master/kafka-docker-example/

## Add Custom Post Processor To The Build Path
Clone Repo:
https://github.com/felixreichenbach/KafkaSinkConnectorCustomizations

Modify the custom post processor java file "DemoFieldAdder.java" in 

```./PostProcessor/src/main/java/com/mongodb/kafka/connect/sink/processor```

Copy the custom post processor java file to the Kafka demo environment build path:

```./src/main/java/com/mongodb/kafka/connect/sink/processor```

## Deploy Custom Post Processor
When you run the Kafka demo environment it will automatically build the Kafka Connector JAR including the custom post processor.

```./run.sh```

## Configure The Sink Connector

```
curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" --data '
  {"name": "mongo-test-sink",
   "config": {
     "connector.class":"com.mongodb.kafka.connect.MongoSinkConnector",
     "tasks.max":"1",
     "topics":"test",
     "connection.uri":"mongodb://mongo1:27017,mongo2:27017,mongo3:27017",
     "database":"custom",
     "collection":"test",
     "key.converter": "org.apache.kafka.connect.storage.StringConverter",
     "value.converter": "org.apache.kafka.connect.storage.StringConverter",
     "value.converter.schemas.enable": "false",
     "post.processor.chain" : "com.mongodb.kafka.connect.sink.processor.DemoFieldAdder"
}}'
```
## Test Custom Write Strategy
We are going to use the command line producer to create messages.
The “broker” container can be used to run Kafka cli commands.

SSH into the broker:

```docker exec -it broker /bin/bash```

Start the command line producer:

```kafka-console-producer --broker-list localhost:9092 --topic test --property value.serializer=custom.class.serialization.JsonSerializer```

Send a message to the topic:
You have to provide a properly formatted JSON string or it will crash! -> Definitely something we can improve!

```{"message":"Hello World"}```

Query MongoDB:
You have to run the command in the folder where you start the demo environment with ./run.sh!

```docker-compose exec mongo1 /usr/bin/mongo```

```
use custom

db.test.find()
```
