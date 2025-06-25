package com.example.membersapp.backend;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BackendConnector {
  private static final Logger LOG = LoggerFactory.getLogger(BackendConnector.class);
  private final WebClient webClient;

  public BackendConnector(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<BackendResponse> getResponse() {
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      LOG.info("Sleep interrupted");
    }
    return webClient
        .get()
        .uri("/backend/request")
        .retrieve()
        .onStatus(
            HttpStatus.NOT_FOUND::equals, // Custom error handling for 404
            response -> Mono.error(new RuntimeException("Post not found: ")))
        .onStatus(
            HttpStatusCode::is4xxClientError, // Generic client error handling
            response -> Mono.error(new RuntimeException("Client Error: " + response.statusCode())))
        .onStatus(
            HttpStatusCode::is5xxServerError, // Generic server error handling
            response -> Mono.error(new RuntimeException("Server Error: " + response.statusCode())))
        .bodyToMono(BackendResponse.class)
        .timeout(Duration.ofSeconds(1)); // Convert the response body to a Mono of Post object
  }
}
