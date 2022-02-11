package com.example.demo.config.kakfa;


import com.google.gson.Gson;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaListenerInterceptor <K, V> implements ConsumerInterceptor<K, V>, ProducerInterceptor<K, V>
{
    private final Logger logger = LoggerFactory.getLogger(KafkaListenerInterceptor.class);

    @Value("${config.service.id}")
    private String serviceId;



    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        Gson gson = new Gson();
        StringBuilder builder = new StringBuilder();
        builder.append(serviceId +" | ");
        builder.append(" | "); //loggerModel.getContentId() +
        builder.append(gson.toJson(records) +" | ");
        builder.append(" | ");

        builder.append(" | ");
        builder.append("");
        builder.append("|");
        String result = builder.toString();
        logger.info(result);

        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }

    @Override
    public void close() {

    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        return null;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }


}
