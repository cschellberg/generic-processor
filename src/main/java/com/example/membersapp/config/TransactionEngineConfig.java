package com.example.membersapp.config;

import static com.example.membersapp.engine.TransactionEngine.INPUT;
import static com.example.membersapp.engine.TransactionEngine.OUTPUT;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.engine.TransactionEngine;
import com.example.membersapp.metric.Metrics;
import com.example.membersapp.nodes.*;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TransactionEngineConfig {

  @Value("${backend.url:http://localhost:9999}")
  private String backendUrl;

  @Value("${authorizer.backend.url:http://localhost:9998}")
  private String authorizerBackendUrl;

  @Bean
  public Metrics metrics(MeterRegistry meterRegistry) {
    return new Metrics(meterRegistry);
  }

  @Bean
  public BackendConnector authorizerBackendConnector(WebClient.Builder webClientBuilder) {
    var webClient =
        webClientBuilder
            .baseUrl(authorizerBackendUrl)
            .defaultHeaders(
                headers -> headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)))
            .build();
    return new BackendConnector(webClient);
  }

  @Bean
  public BackendConnector backendConnector(WebClient.Builder webClientBuilder) {
    var webClient =
        webClientBuilder
            .baseUrl(backendUrl)
            .defaultHeaders(
                headers -> headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)))
            .build();
    return new BackendConnector(webClient);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getTreeNode(
      @Qualifier("backendConnector") BackendConnector backendConnector, Metrics metrics) {
    return new TreeNode(backendConnector, metrics);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getAuthorizerNode(
      @Qualifier("authorizerBackendConnector") BackendConnector backendConnector, Metrics metrics) {
    return new AuthorizerNode(backendConnector, metrics);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getPurchaseNode(
      @Qualifier("authorizerBackendConnector") BackendConnector backendConnector, Metrics metrics) {
    return new PurchaseNode(backendConnector, metrics);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getTransactionOutputNode(Metrics metrics) {
    return new TransactionOutputNode(OUTPUT, metrics);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getRouterNode(Metrics metrics) {
    return new RouterNode(INPUT, metrics);
  }

  @Bean
  @Scope("prototype")
  public TransactionEngine getTransactionEngine() {
    return new TransactionEngine();
  }
}
