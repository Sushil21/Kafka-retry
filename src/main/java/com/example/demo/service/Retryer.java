package com.example.demo.service;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
class Retryer {

    @Retryable(maxAttempts = 3,value = { IllegalStateException.class })
    public void invoke() {
        System.out.println("Invoked");
        throw new IllegalStateException("failed");
    }

    @Recover
    public void recover(IllegalStateException e) {
        System.out.println("Retries exhausted");
        throw new RuntimeException(e);
    }

}