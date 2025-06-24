package com.example.membersapp.controller;

import com.example.membersapp.engine.TransactionEngine;
import com.example.membersapp.engine.TransactionEngineManager;
import com.example.membersapp.model.Member;
import com.example.membersapp.model.Transaction;
import com.example.membersapp.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

  @Autowired private TransactionEngineManager transactionEngineManager;

  @PostMapping
  public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) throws ClassNotFoundException, ExecutionException, InterruptedException, TimeoutException {
    var transactionEngine=transactionEngineManager.getTransactionEngine("test_nodes");
    Map<String,Object> map=null;
    try {
      map=transactionEngine.execute(transaction);
    }catch (Exception e){
      transaction.setResponse("099");
      transaction.setResponseDescription(e.getMessage());
      return new ResponseEntity<>(transaction, HttpStatus.NOT_ACCEPTABLE);
    }
    LOG.info("Transaction created {}",map);
    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
  }


}
