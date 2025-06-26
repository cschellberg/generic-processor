package com.example.membersapp.model;

import static com.example.membersapp.model.Message.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class Transaction {
  private String transactionId;
  private Date transactionDate;
  private String account;
  private Double amount;
  private String route;
  private String operation;

  @JsonProperty(TRANSACTION_ID)
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @JsonProperty(TRANSACTION_DATE)
  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  @JsonProperty(ACCOUNT_NUMBER)
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  @JsonProperty(TRANSACTION_AMOUNT)
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @JsonProperty(ROUTE)
  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  @JsonProperty(OPERATION)
  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }
}
