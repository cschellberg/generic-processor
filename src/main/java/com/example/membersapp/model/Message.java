package com.example.membersapp.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class Message {
  private ObjectMapper objectMapper = new ObjectMapper();
  private Map<String, Object> scratchPadMap = new HashMap<>();
  private Map<String, Object> requestMap = new HashMap<>();
  private Map<String, Object> responseMap = new HashMap<>();

  public static final String REQUEST = "request";
  public static final String RESPONSE = "response";
  public static final String SCRATCH_PAD = "scratch_pad";
  public static final String RESPONSE_CODE = "response_code";
  public static final String RESPONSE_DESCRIPTION = "response_description";

  public Message(Object request) {
    var jsonMap = objectMapper.convertValue(request, Map.class);
    requestMap.put(REQUEST, jsonMap);
  }

  public void addToScratchPad(String key, Object value) {
    scratchPadMap.put(key, value);
  }

  @Override
  public String toString() {
    return "Message{" +
            "scratchPadMap=" + scratchPadMap +
            ", requestMap=" + requestMap +
            ", responseMap=" + responseMap +
            '}';
  }
}
