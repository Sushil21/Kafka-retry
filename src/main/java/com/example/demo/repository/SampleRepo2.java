package com.example.demo.repository;


import com.example.demo.model.Sample;
import com.example.demo.model.Sample2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepo2 extends JpaRepository<Sample2, Long> {
    Sample findFirstById(Long id);

}