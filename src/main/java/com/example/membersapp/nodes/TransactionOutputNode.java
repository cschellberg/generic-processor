package com.example.membersapp.nodes;

import static com.example.membersapp.model.Message.*;

import com.example.membersapp.model.Message;
import com.example.membersapp.model.Metric;
import com.example.membersapp.model.TransactionResponse;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionOutputNode extends TreeNode {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionOutputNode.class);

  public TransactionOutputNode(String name) {
    super(name);
  }

  protected void executeBody(Message message, CompletableFuture<Void> future, Metric metric) {
    var transactionResponse = getResponse(message);
    message.setResponse(transactionResponse);
    metric.endWithSuccess();
    future.complete(null);
  }

  private Object getResponse(Message message) {
    var transactionResponse = new TransactionResponse();
    var scratchPadMap = message.getScratchPadMap();
    transactionResponse.setTransactionId((String) scratchPadMap.get(TRANSACTION_ID));
    transactionResponse.setTransactionAmount((Double) scratchPadMap.get(TRANSACTION_AMOUNT));
    transactionResponse.setAccountNumber((String) scratchPadMap.get(ACCOUNT_NUMBER));
    transactionResponse.setTransactionDate(
        (new Date((Long) scratchPadMap.get(TRANSACTION_DATE))).toString());
    transactionResponse.setResponseCode((String) scratchPadMap.get(RESPONSE_CODE));
    transactionResponse.setResponseMessage((String) scratchPadMap.get(RESPONSE_DESCRIPTION));
    transactionResponse.setOperation((String) scratchPadMap.get(OPERATION));
    return transactionResponse;
  }
}
