package com.example.membersapp.orchestrator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class NodeDetails {
  private List<String> children;
  private List<String> dependsOn;
  private String className;

  // Default constructor for Jackson
  public NodeDetails() {}

  // Constructor to handle potential missing 'children' key gracefully
  @JsonCreator
  public NodeDetails(
      @JsonProperty("children") List<String> children,
      @JsonProperty("dependsOn") List<String> dependsOn,
      @JsonProperty("className") String className) {
    this.children = children;
    this.dependsOn = dependsOn;
    this.className = className;
  }

  @Override
  public String toString() {
    return "NodeDetails{"
        + "children="
        + children
        + ", dependsOn="
        + dependsOn
        + ", className='"
        + className
        + '\''
        + '}';
  }
}
