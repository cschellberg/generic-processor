package com.example.membersapp.orchestrator;

import java.util.List;
import java.util.Map;

public class NodeConfig {
  private List<Map<String, NodeDetails>> nodes;

  public List<Map<String, NodeDetails>> getNodes() {
    return nodes;
  }

  public void setNodes(List<Map<String, NodeDetails>> nodes) {
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Config{\n");
    if (nodes != null) {
      for (Map<String, NodeDetails> nodeMap : nodes) {
        nodeMap.forEach(
            (nodeName, details) ->
                sb.append("  ").append(nodeName).append(": ").append(details).append("\n"));
      }
    } else {
      sb.append("  nodes: null\n");
    }
    sb.append('}');
    return sb.toString();
  }
}
