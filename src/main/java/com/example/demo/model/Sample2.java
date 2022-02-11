package com.example.demo.model;

import javax.persistence.*;

@Entity
    @Table(name = "SAMPLE_DATA2")
    public class Sample2 {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private long id;
        @Column(name = "FIRST_NAME")
        private String firstName;

        public Sample2(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "Task [id=" + id + ", firstName=" + firstName + "]";
        }
    }

