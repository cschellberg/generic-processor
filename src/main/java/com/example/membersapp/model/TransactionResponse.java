package com.example.membersapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
  private String transactionId;
  private String transactionDate;
  private String operation;
  private Double transactionAmount;
  private String responseCode;
  private String responseMessage;
  private String accountNumber;
}
