# Kafka-retry

1. start zookeeper
2. start kafka

call api:

localhost:9092/test ::> this api will send some data to kafka topic then consumer will consume same. during consuming forcefully throwing exception
