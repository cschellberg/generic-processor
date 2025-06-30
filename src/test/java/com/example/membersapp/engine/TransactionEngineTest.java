package com.example.membersapp.engine;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.membersapp.model.Message;
import com.example.membersapp.model.Transaction;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(properties = "backend.url=http://localhost:8888")
public class TransactionEngineTest {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionEngineTest.class);
  @Autowired private TransactionEngineManager transactionEngineManager;
  @Autowired private ApplicationContext applicationContext;
  private TransactionEngine transactionEngine;
  private static WireMockServer server;

  @BeforeAll
  public static void init() {
    server = new WireMockServer(wireMockConfig().port(8888));
    server.start();
    server.stubFor(
        get(urlEqualTo("/backend/request"))
            .willReturn(
                aResponse() // return this response
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody("{\"response\":\"hello\"}")));
    LOG.info("WireMock Server started");
  }

  @AfterAll
  public static void stop() {
    server.stop();
  }

  @BeforeEach
  void setUp() throws ClassNotFoundException {
    if (transactionEngine == null) {
      var transactionEngineManager = applicationContext.getBean(TransactionEngineManager.class);
      transactionEngine = transactionEngineManager.getTransactionEngine("usda");
    }
  }

  @Test
  public void testTransaction() throws Exception {
    executeTransaction();
  }

  @Test
  public void performanceTest() {
    long timer = System.currentTimeMillis();
    var futureList = new ArrayList<CompletableFuture<Void>>();
    for (int i = 0; i < 100; i++) {
      futureList.add(
          CompletableFuture.runAsync(
              () -> {
                try {
                  executeTransaction();
                } catch (Exception e) {
                  fail("Performance test failed on execution");
                }
              }));
    }
    CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    LOG.info("Performance test completed in {} ms", (System.currentTimeMillis() - timer));
  }

  private void executeTransaction() {
    try {
      var transaction = new Transaction();
      transaction.setAccount("123456789012345");
      transaction.setAmount(199.99);
      transaction.setTransactionDate(new Date());
      transaction.setTransactionId("123456");
      transaction.setOperation("authorization");
      var message = new Message(transaction);
      transactionEngine.execute(message);
      LOG.info("Transaction Successfully Executed with metrics ");
    } catch (Exception ex) {
      LOG.error("Unable to execute tree node because {}", ex.getMessage(), ex);
    }
  }
}
