package com.example.membersapp.controller;

import com.example.membersapp.engine.TransactionEngineManager;
import com.example.membersapp.model.Message;
import com.example.membersapp.model.Transaction;
import com.example.membersapp.model.TransactionResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

  @Autowired private TransactionEngineManager transactionEngineManager;

  @PostMapping
  public ResponseEntity<TransactionResponse> createTransaction(@RequestBody Transaction transaction)
      throws ClassNotFoundException, ExecutionException, InterruptedException, TimeoutException {
    var transactionEngine = transactionEngineManager.getTransactionEngine(transaction.getRoute());
    Message message = null;
    LOG.info("transactionEngine: {} ", transactionEngine);
    TransactionResponse transactionResponse = new TransactionResponse();
    try {
      message = transactionEngine.execute(transaction);
      transactionResponse = (TransactionResponse) message.getResponse();
    } catch (Exception e) {
      transactionResponse.setResponseCode("909");
      transactionResponse.setResponseMessage("Declined because of server error");
      return new ResponseEntity<>(transactionResponse, HttpStatus.NOT_ACCEPTABLE);
    }
    LOG.info("Transaction created {}", message);
    return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
  }
}
