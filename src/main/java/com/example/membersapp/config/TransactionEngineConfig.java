package com.example.membersapp.config;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.engine.TransactionEngine;
import com.example.membersapp.nodes.TreeNode;
import java.util.Collections;
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
  public TreeNode getTreeNode(BackendConnector backendConnector) {
    var treeNode = new TreeNode(backendConnector);
    return treeNode;
  }

  @Bean
  @Scope("prototype")
  public TransactionEngine getTransactionEngine() {
    return new TransactionEngine();
  }
}
