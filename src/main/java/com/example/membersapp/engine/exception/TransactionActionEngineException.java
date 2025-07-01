package com.example.membersapp.engine.exception;

import static com.example.membersapp.model.Message.*;

import com.example.membersapp.model.Message;
import com.example.membersapp.nodes.TransactionOutputNode;

public class TransactionActionEngineException extends Exception {
  private final Message transactionMessage;

  public TransactionActionEngineException(Message transactionMessage) {
    this.transactionMessage = transactionMessage;
  }

  public Message getTransactionMessage() {
    return transactionMessage;
  }

  public Object getResponse() {
    return TransactionOutputNode.getResponse(transactionMessage);
  }
}
