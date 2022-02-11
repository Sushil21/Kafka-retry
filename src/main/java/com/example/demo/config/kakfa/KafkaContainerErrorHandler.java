package com.example.demo.config.kakfa;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class KafkaContainerErrorHandler implements ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(KafkaContainerErrorHandler.class);

    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];

        // modify below logic according to your topic nomenclature
        String topics = s.substring(0, s.lastIndexOf('-'));
        int offset = Integer.parseInt(s.split("offset ")[1]);
        int partition = Integer.parseInt(s.substring(s.lastIndexOf('-') + 1).split(" at")[0]);

        logger.error("KAFKA Error-Found at partition {} offset {} ", partition, offset);
        TopicPartition topicPartition = new TopicPartition(topics, partition);
        logger.info("Skipping {} - {} offset {}",  topics, partition, offset);
        consumer.seek(topicPartition, offset + 1);
    }

    @Override
    public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {

    }
}
