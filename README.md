# KafkaSinkConnectorCustomizations

The MongoDB Kafka Sink connector provides out of the box write strategies and post processors and it is recommended to use them as much as possible. 
Now there may be good reasons to build your own to accomodate your sepcific requirements.

The intention of this repo is to provide you the foundation to get started quickly and allow you efficiently building little demos for TDDs or even PoV items.

There are two parts in this repo, "How to build a PostProcessor" and "How to build a WriteStrategy". Both build upon the Mongo-Kafka Demo environment.

## Prerequisit: Demo Environment
To be able to easily deploy and test your customizations. We recommend to follow this guide:
http://docs.mongodb.com/kafka-connector/master/kafka-docker-example/


## PostProcessor
A post processor allows you to transform / change messages, before they are handed over to one of the write strategies.
You can find detailed instructions here:
https://github.com/felixreichenbach/KafkaSinkConnectorCustomizations/tree/master/PostProcessor

## WriteStrategy
A write strategy defines the write command executed against the mongodb environment.
You can find detailed information here:
https://github.com/felixreichenbach/KafkaSinkConnectorCustomizations/tree/master/WriteStrategy
