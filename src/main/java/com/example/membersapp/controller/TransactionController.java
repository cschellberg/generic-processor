package com.example.membersapp.controller;

import com.example.membersapp.engine.TransactionEngineManager;
import com.example.membersapp.model.Transaction;
import java.util.Map;
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
  public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction)
      throws ClassNotFoundException, ExecutionException, InterruptedException, TimeoutException {
    var transactionEngine = transactionEngineManager.getTransactionEngine(transaction.getRoute());
    Map<String, Object> map = null;
    try {
      map = transactionEngine.execute(transaction);
    } catch (Exception e) {
      transaction.setResponse("099");
      transaction.setResponseDescription(e.getMessage());
      return new ResponseEntity<>(transaction, HttpStatus.NOT_ACCEPTABLE);
    }
    LOG.info("Transaction created {}", map);
    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
  }
}
