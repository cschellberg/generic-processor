package com.example.membersapp.model;

import java.util.Date;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Transaction {
  private String transactionId;
  private Date transactionDate;
  private final String account;
  private final Double amount;
  private String response;
  private String responseDescription;
}
