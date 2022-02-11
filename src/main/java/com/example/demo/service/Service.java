package com.example.demo.service;

import com.example.demo.model.Sample;
import com.example.demo.repository.SampleRepo2;
import com.example.demo.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
class Service {

    @Autowired
    Retryer retryable;

    @Autowired
    SampleRepository sampleRepository;
    @Autowired
    SampleRepo2 sampleRepo2;

    @Transactional(rollbackFor = Exception.class)
    public void process() {
        Sample sample = new Sample("sushil");
        sampleRepository.save(sample);
        retryable.invoke();
    }

}