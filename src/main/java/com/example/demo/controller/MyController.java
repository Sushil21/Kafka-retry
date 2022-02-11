package com.example.demo.controller;


import com.example.demo.config.kakfa.Producer;
import com.example.demo.model.SampleKafkaModel;
import com.example.demo.model.SamplekafkaModel2;
import com.example.demo.service.ServiceCaller;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
public class MyController {

	@Autowired
	ServiceCaller serviceCaller;

	@Autowired
	private KafkaTemplate<String, SampleKafkaModel> kafkaTemplate;

	@Autowired
	Producer producer;

	 @GetMapping(path ="/test")
	    public String getData()
	    {

			try {

//				Properties properties = new Properties();
//				properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//
//				AdminClient adminClient = AdminClient.create(properties);
//
//				ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
//				listTopicsOptions.listInternal(true);
//
//				System.out.println("topics:" + adminClient.listTopics(listTopicsOptions).names().get());
				SampleKafkaModel sampleKafkaModel = new SampleKafkaModel();
				sampleKafkaModel.setAge("30");
				sampleKafkaModel.setName("sushil");

				SamplekafkaModel2 samplekafkaModel2 = new SamplekafkaModel2();
				samplekafkaModel2.setAge1("30");
				samplekafkaModel2.setName1("mittal");
				producer.produce("test-topic3",null,sampleKafkaModel);
				//serviceCaller.call();
			}catch (Exception ee){
				System.out.println(ee);
			}
		  return "hello";


	    }
	 

}