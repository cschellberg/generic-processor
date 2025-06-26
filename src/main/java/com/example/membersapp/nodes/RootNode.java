package com.example.membersapp.nodes;

import static com.example.membersapp.engine.TransactionEngine.ROOT;

import com.example.membersapp.model.Message;
import com.example.membersapp.model.Metric;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RootNode extends TreeNode {

  public RootNode() {
    super(ROOT);
  }

  @Override
  public CompletableFuture<Void> execute(Message message, List<Metric> metricList) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    setNextNode(message);
    future.complete(null);
    return future;
  }

  @Override
  protected void setNextNode(Message message) {
    nextNode = this.getChildren().getFirst().getName();
  }
}
