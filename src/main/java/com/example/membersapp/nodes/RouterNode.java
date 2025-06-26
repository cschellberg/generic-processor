package com.example.membersapp.nodes;

import static com.example.membersapp.model.Message.OPERATION;

import com.example.membersapp.model.Message;
import com.example.membersapp.model.Metric;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RouterNode extends TreeNode {

  public RouterNode(String name) {
    super(name);
  }

  @Override
  public CompletableFuture<Void> execute(Message message, List<Metric> metricList) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    setNextNode(message);
    future.complete(null);
    return future;
  }

  protected void setNextNode(Message message) {
    nextNode = (String) message.getScratchPadMap().get(OPERATION);
  }
}
