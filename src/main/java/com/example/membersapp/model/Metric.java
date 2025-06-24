package com.example.membersapp.model;

import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class Metric {
  @Setter(AccessLevel.NONE)
  private Long initialization = System.currentTimeMillis();

  @Setter(AccessLevel.NONE)
  private final String nodeName;

  @Setter(AccessLevel.NONE)
  private Long durationInMillis;

  private Boolean success;

  public void endWithSuccess() {
    durationInMillis = System.currentTimeMillis() - initialization;
    success = true;
  }

  public void endWithFailure() {
    durationInMillis = System.currentTimeMillis() - initialization;
    success = false;
  }

  @Override
  public String toString() {
    return "Metric{"
        + "nodeName='"
        + nodeName
        + '\''
        + ",initialization="
        + new Date(initialization)
        + ", durationInMillis="
        + durationInMillis
        + ", success="
        + success
        + '}';
  }
}
