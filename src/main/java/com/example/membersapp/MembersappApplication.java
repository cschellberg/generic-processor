package com.example.membersapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MembersappApplication {

  private static final Logger LOG = LoggerFactory.getLogger(MembersappApplication.class);

  public static void main(String[] args) {
    try {
      SpringApplication.run(MembersappApplication.class, args);
    } catch (Exception e) {
      LOG.error("Unable to load spring contect because {}", e.getMessage(), e);
    }
  }
}
