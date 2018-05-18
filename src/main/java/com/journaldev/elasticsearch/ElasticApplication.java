package com.journaldev.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.journaldev.elasticsearch.dao.jpa")
public class ElasticApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElasticApplication.class, args);
  }

}