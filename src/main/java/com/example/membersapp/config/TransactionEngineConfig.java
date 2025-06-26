package com.example.membersapp.config;

import static com.example.membersapp.engine.TransactionEngine.OUTPUT;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.engine.TransactionEngine;
import com.example.membersapp.nodes.AuthorizerNode;
import com.example.membersapp.nodes.PurchaseNode;
import com.example.membersapp.nodes.TransactionOutputNode;
import com.example.membersapp.nodes.TreeNode;
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
  public TreeNode getTreeNode(@Qualifier("backendConnector") BackendConnector backendConnector) {
    return new TreeNode(backendConnector);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getAuthorizerNode(
      @Qualifier("authorizerBackendConnector") BackendConnector backendConnector) {
    return new AuthorizerNode(backendConnector);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getPurchaseNode(
      @Qualifier("authorizerBackendConnector") BackendConnector backendConnector) {
    return new PurchaseNode(backendConnector);
  }

  @Bean
  @Scope("prototype")
  public TreeNode getTransactionOutputNode() {
    return new TransactionOutputNode(OUTPUT);
  }

  @Bean
  @Scope("prototype")
  public TransactionEngine getTransactionEngine() {
    return new TransactionEngine();
  }
}
