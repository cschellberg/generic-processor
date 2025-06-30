package com.example.membersapp.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Message {
  private static final Logger LOG = LoggerFactory.getLogger(Message.class);
  private ObjectMapper objectMapper = new ObjectMapper();
  @Getter private final Map<String, Object> scratchPadMap = new HashMap<>();

  @Setter @Getter private Object response;
  @Setter @Getter private Object request;

  public static final String RESPONSE_CODE = "response_code";
  public static final String RESPONSE_DESCRIPTION = "response_description";
  public static final String OPERATION = "operation";

  public static final String TRANSACTION_ID = "transaction_id";
  public static final String TRANSACTION_DATE = "transaction_date";
  public static final String ACCOUNT_NUMBER = "account_number";
  public static final String TRANSACTION_AMOUNT = "transaction_amount";
  public static final String ROUTE = "route";

  public Message(Object request) {
    this.request = request;
    convertPojoToMapWithCustomKeys(request);
  }

  public void addToScratchPad(String key, Object value) {
    scratchPadMap.put(key, value);
  }

  public <T> void convertPojoToMapWithCustomKeys(T sourcePojo) {
    Map<String, Object> targetMap = new HashMap<>();

    // Basic validation for input parameters
    if (sourcePojo == null) {
      System.err.println("Warning: Source POJO cannot be null. Returning an empty map.");
      return;
    }
    try {
      var map = objectMapper.convertValue(sourcePojo, Map.class);
      scratchPadMap.putAll(map);
    } catch (IllegalArgumentException e) {
      System.err.println("Error during POJO to Map conversion with Jackson: " + e.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Message{"
        + ", scratchPadMap="
        + scratchPadMap
        + ", response="
        + response
        + ", request="
        + request
        + '}';
  }
}
