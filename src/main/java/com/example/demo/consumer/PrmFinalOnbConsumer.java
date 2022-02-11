package com.example.demo.consumer;


import com.example.demo.config.kakfa.Producer;
import com.example.demo.model.SampleKafkaModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Service
//@ConditionalOnProperty(value = "consumer.isenabled",matchIfMissing = false)
public class PrmFinalOnbConsumer {

    private static final Logger log = LoggerFactory.getLogger(PrmFinalOnbConsumer.class);
    private static final String RETRY = "_RETRY1";

//    @Autowired
//    private PrmSignupFinalDataVerification prmSignupFinalDataVerification;

    @Autowired
    Producer producer;

    @KafkaListener(
            topics = "${kafka.retry.sample.topic}",
            groupId = "${group.id}",
            containerFactory = "kafkaStatefulRetryListenerContainerFactory")
    public void consumePrmFinalOnb(ConsumerRecord<String, SampleKafkaModel> consumerRecord, SampleKafkaModel prmSignupFinalModel) throws Exception {
        System.out.println("helloooo");
            log.info("Topic:{}, Partition:{}, Offset:{}, key:{}, records{}", consumerRecord.topic(),
                    consumerRecord.partition(), consumerRecord.offset(), consumerRecord.key(), prmSignupFinalModel);

            throw new ArithmeticException();
//            try {
//                int i = 1 / 0;
//            }catch (Exception ee){
//                System.out.println(ee);
//                producer.produce(consumerRecord.topic(),null,prmSignupFinalModel);
//            }
        //prmSignupFinalDataVerification.signupDataVerification(prmSignupFinalModel);

    }


//    @KafkaListener(
//            topics = "test-topic1_sample-consumer-group_RETRY",
//            groupId = "abc",
//            containerFactory = "getSampleRetryListenerContainerFactory")
//    public void consumePrmFinalOnbRetry(ConsumerRecord<String, SampleKafkaModel> consumerRecord, SampleKafkaModel prmSignupFinalModel) throws Exception {
//        System.out.println("helloooo222");
//        log.info("********Topic:{}, Partition:{}, Offset:{}, key:{}, records{}", consumerRecord.topic(),
//                consumerRecord.partition(), consumerRecord.offset(), consumerRecord.key(), prmSignupFinalModel);
//
//        try {
//            int i = 1 / 0;
//        }catch (Exception ee){
//            System.out.println(ee);
//          //  producer.produce(consumerRecord.topic()+RETRY,null,prmSignupFinalModel);
//        }
//        //prmSignupFinalDataVerification.signupDataVerification(prmSignupFinalModel);
//
//    }
}
