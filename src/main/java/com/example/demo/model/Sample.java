package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "SAMPLE_DATA")
public class Sample {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	@Column(name = "FIRST_NAME")
	private String firstName;

	public Sample(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", firstName=" + firstName + "]";
	}
}