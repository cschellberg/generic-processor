package com.example.membersapp.nodes;

import static com.example.membersapp.engine.TransactionEngine.ROOT;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.metric.Metrics;
import com.example.membersapp.model.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class TreeNode implements TreeNodeInterface {
  private static final Logger LOG = LoggerFactory.getLogger(TreeNode.class);
  protected String name;
  protected String nextNode;
  protected Metrics metrics;
  private final List<TreeNodeInterface> children = new ArrayList<>();
  protected BackendConnector backendConnector;
  private List<String> dependsOn = new ArrayList<>();

  public TreeNode(String name, Metrics metrics) {
    this.name = name;
    this.metrics = metrics;
  }

  public TreeNode(BackendConnector backendConnector, Metrics metrics) {
    this.backendConnector = backendConnector;
    this.metrics = metrics;
  }

  @Override
  public CompletableFuture<Void> execute(Message message) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    setNextNode(message);
    if (!this.name.equals(ROOT)) {
      metrics
          .getTimer(this.name)
          .record(
              () -> {
                executeBody(message, future);
              });
    } else {
      future.complete(null);
    }
    return future;
  }

  protected void executeBody(Message message, CompletableFuture<Void> future) {
    if (backendConnector != null) {
      backendConnector
          .getResponse()
          .subscribe(
              response -> {
                LOG.info("Node {} is executing with workingMap {}", name, message);
                metrics.addSuccess(this.name);
                future.complete(null);
              },
              error -> {
                LOG.error("Node {} failed execution because {}", name, error.toString());
                metrics.addFalures(this.name);
                future.completeExceptionally(error);
              });
    } else {
      metrics.addSuccess(this.name);
      future.complete(null);
    }
  }

  protected void setNextNode(Message message) {
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
