package com.example.demo.repository;

import com.example.demo.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
	Sample findFirstById(Long id);

}