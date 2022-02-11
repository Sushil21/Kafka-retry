package com.example.demo.config.kakfa;


import com.example.demo.model.SampleKafkaModel;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EnableKafka
@Configuration
@ConditionalOnProperty(value = "consumer.isenabled",matchIfMissing = false)
public class KafkaConsumerConfig {

    @Value(value = "${common.prm.final.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${common.prm.final.kafka.numOfKafkaListeners}")
    private int numOfKafkaListeners;

    @Value(value = "${kafka.heartbeat.interval}")
    private String kafkaHearbeatIntervalMs;

    @Value(value = "${kafka.max.poll.interval.ms}")
    private String kafkaMaxPollIntervalMs;

    @Value(value = "${common.prm.final.kafka.autocommit.interval}")
    private int autoCommitInterval;

    @Autowired
    KafkaContainerErrorHandler kafkaContainerErrorHandler;


    @Bean
    public AsyncListenableTaskExecutor getPrmFinalOnbTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(numOfKafkaListeners);
        return taskExecutor;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> getSampleRetryListenerContainerFactory()
    {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaListenerInterceptor.class.getName());
        ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, SampleKafkaModel>(props, new StringDeserializer(),
                new JsonDeserializer(SampleKafkaModel.class)));

        factory.getContainerProperties().setConsumerTaskExecutor(getPrmFinalOnbTaskExecutor());
        factory.setErrorHandler(kafkaContainerErrorHandler);

        return factory;
    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> getSampleRetryListenerContainerFactoryRetry()
//    {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaListenerInterceptor.class.getName());
//        ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, SampleKafkaModel>(props, new StringDeserializer(),
//                new JsonDeserializer(SampleKafkaModel.class)));
//        factory.setRetryTemplate(retryTemplate());
//        factory.getContainerProperties().setConsumerTaskExecutor(getPrmFinalOnbTaskExecutor());
//        factory.setRecoveryCallback(retryContext -> {
//            System.out.println("recovery call back");
//           return Optional.empty();
//        });
//       // factory.setErrorHandler(kafkaContainerErrorHandler);
//
//        return factory;
//    }

//    public RetryTemplate retryTemplate(){
//        RetryTemplate retryTemplate = new RetryTemplate();
//        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//        fixedBackOffPolicy.setBackOffPeriod(3000);
//        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//        SimpleRetryPolicy simpleRetryPolicy =  new SimpleRetryPolicy();
//        simpleRetryPolicy.setMaxAttempts(3);
//        retryTemplate.setRetryPolicy(simpleRetryPolicy);
//        return retryTemplate;
//    }


//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                "kafka_event_id");
//        props.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        props.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String>
//    kafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> kafkaStatefulRetryListenerContainerFactory() {

        final SeekToCurrentErrorHandler errorHandler =
                new SeekToCurrentErrorHandler((record, exception) -> {
                    // 4 seconds pause, 4 retries.
                }, new FixedBackOff(4000L, 4L));
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaListenerInterceptor.class.getName());

        final ConcurrentKafkaListenerContainerFactory<String, SampleKafkaModel> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, SampleKafkaModel>(props, new StringDeserializer(),
                new JsonDeserializer(SampleKafkaModel.class)));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .fixedBackoff(4000)
                .maxAttempts(5)
                .build();
    }

}