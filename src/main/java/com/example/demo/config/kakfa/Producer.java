package com.example.demo.config.kakfa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class Producer<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
	@Autowired
	@Qualifier("kafkaTemplateCluster")
	private KafkaTemplate<String, T> kafkaTemplate;

	public void pushOnKafka(String topic, String key, T payload) {
		try {
			kafkaTemplate.send(topic, key, payload);
		} catch (Exception e) {
			LOGGER.error("Error occurred while producing on kafka with topic : " + topic, e);
		}

	}

	public void produce(String topic, String key, T payload) {

		ListenableFuture<SendResult<String, T>> future = kafkaTemplate.send(topic, key, payload);

		future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {

			@Override
			public void onSuccess(SendResult<String, T> payload) {
				onSuccessHandler(payload);
			}

			@Override
			public void onFailure(Throwable th) {
				onFailureHandler(th, topic, payload);
			}
		});
	}

//	public void produceInNewCluster(String topic, String key, Object payload) {
//
//		ListenableFuture<SendResult<String, Object>> future = kafkaTemplateNewCluster.send(topic, key, payload);
//
//		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//
//			@Override
//			public void onSuccess(SendResult<String, Object> payload) {
//				onSuccessHandler(payload);
//			}
//
//			@Override
//			public void onFailure(Throwable th) {
//				onFailureHandler(th, topic, payload);
//			}
//		});
//	}

	protected void onSuccessHandler(SendResult<String, T> payload) {
		LOGGER.info("Record produced successfully. Topic: {}, Payload: {}", payload.toString());
	}

	protected void onFailureHandler(Throwable th, String topic, T payload) {
		LOGGER.error("Failed to produced record for Topic: {}, Payload: {}, exception: {}", topic, payload, th);
	}

}
