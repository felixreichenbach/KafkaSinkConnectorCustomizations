# How to build a custom WriteStrategy
A simple MongoDB Kafka connector custom write strategy to get you started.


## Prerequisites
Get the Kafka connector demo environment up and running:
http://docs.mongodb.com/kafka-connector/master/kafka-docker-example/

## Build Custom WriteStrategy JAR File
Clone Repo:
https://github.com/felixreichenbach/KafkaSinkConnectorCustomizations

Cd into the "./WriteStrategy" directory and run:

```mvn clean install```

## Deploy Custom Write Strategy JAR File
Copy the jar file into the "connect" container:

```docker cp ./target/kafka-write-strategy-1.0-SNAPSHOT.jar connect:/usr/share/confluent-hub-components/kafka-connect-mongodb/lib/kafka-write-strategy-1.0-SNAPSHOT.jar```


Restart the container “connect”:

```docker restart connect```

## Configure the MongoDB-Kafka-Connector

Create MongoDB Sink Connector (without custom write strategy!)
Note: 
I am using the “StringConverter” which means the message must contain a valid json structure!

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
     "writemodel.strategy": "de.demo.kafka.CustomWriteModelStrategy"
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

**Other useful Kafka cli commands:**

List topics:

```kafka-topics --list --bootstrap-server localhost:9092```

Create a topic:

```kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test```

Delete topic:

```kafka-topics --delete --bootstrap-server localhost:9092 --topic test```

## Deploy Code Changes

You can now easily change the java code, rebuild it, copy it to the "connect" container and restart it:

```
mvn clean install

docker cp ./target/kafka-write-strategy-1.0-SNAPSHOT.jar connect:/usr/share/confluent-hub-components/kafka-connect-mongodb/lib/kafka-write-strategy-1.0-SNAPSHOT.jar

docker restart connect
```

