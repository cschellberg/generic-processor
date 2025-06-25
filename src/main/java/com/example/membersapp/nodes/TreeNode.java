package com.example.membersapp.nodes;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.model.Metric;
import com.example.membersapp.model.TransactionResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class TreeNode implements TreeNodeInterface {
  private static final Logger LOG = LoggerFactory.getLogger(TreeNode.class);
  protected String name;
  private String nextNode;
  private final List<TreeNodeInterface> children = new ArrayList<>();
  protected BackendConnector backendConnector;
  private List<String> dependsOn = new ArrayList<>();

  public TreeNode(String name) {
    this.name = name;
  }

  public TreeNode(BackendConnector backendConnector) {
    this.backendConnector = backendConnector;
  }

  @Override
  public CompletableFuture<TransactionResponse> execute(
      Map<String, Object> workingMap, List<Metric> metricList) {
    CompletableFuture<TransactionResponse> future = new CompletableFuture<>();
    setNextNode();
    var metric = new Metric(name);
    metricList.add(metric);
    executeBody(workingMap, future, metric);
    return future;
  }

  protected void executeBody(
      Map<String, Object> workingMap,
      CompletableFuture<TransactionResponse> future,
      Metric metric) {
    if (backendConnector != null) {
      backendConnector
          .getResponse()
          .subscribe(
              response -> {
                LOG.info("Node {} is executing with workingMap {}", name, workingMap);
                metric.endWithSuccess();
                future.complete(new TransactionResponse(response.getResponse()));
              },
              error -> {
                LOG.error("Node {} failed execution because {}", name, error.toString());
                metric.endWithFailure();
                future.completeExceptionally(error);
              });
    } else {
      metric.endWithSuccess();
      future.complete(new TransactionResponse("no backend connector available"));
    }
  }

  protected void setNextNode() {
    var random = new Random();
    if (!children.isEmpty()) {
      nextNode = (children.get(random.nextInt(children.size())).getName());
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addChild(TreeNodeInterface child) {
    this.children.add(child);
  }

  @Override
  public List<TreeNodeInterface> getChildren() {
    return this.children;
  }
}
