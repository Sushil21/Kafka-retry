package com.example.demo.config.kakfa;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducersConfig<T> {

	@Value(value = "${common.prm.final.kafka.bootstrapAddress}")
	private String bootstrapAddress;


	@Bean
	public ProducerFactory<String, T> producerFactory(String kafkaBootstrapAddress) {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
		/*
		 * configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		 * configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		 * configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
		 */
		// configProps.put(ConsumerConfig.METRIC_REPORTER_CLASSES_CONFIG,"io.micrometer.core.instrument.binder.kafka.KafkaProducerApiMetrics");
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Primary
	@Bean(name = "kafkaTemplateCluster")
	public KafkaTemplate<String, T> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory(bootstrapAddress));
	}

//	@Bean(name = "kafkaTemplateNewCluster")
//	public KafkaTemplate<String, T> kafkaTemplatePaymentHubCluster() {
//		return new KafkaTemplate<>(producerFactory(newBootstrapAddress));
//	}

}
