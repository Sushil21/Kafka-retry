package com.example.demo.model;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.util.Map;

public class SampleKafkaModel{
    String name;
    String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SampleKafkaModel{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
