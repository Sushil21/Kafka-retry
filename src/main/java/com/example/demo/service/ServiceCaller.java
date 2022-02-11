package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceCaller {

    @Autowired
    Service service;

    public void call() {
        try {
            this.service.process();
        }catch (RuntimeException e) {
            throw e;
        }
    }

}