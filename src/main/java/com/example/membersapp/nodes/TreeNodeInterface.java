package com.example.membersapp.nodes;

import com.example.membersapp.model.Message;
import com.example.membersapp.model.Metric;
import com.example.membersapp.model.TransactionResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TreeNodeInterface {

  CompletableFuture<TransactionResponse> execute(Message message, List<Metric> metricList);

  String getName();

  void setName(String name);

  void setDependsOn(List<String> dependsOn);

  void addChild(TreeNodeInterface child);

  List<TreeNodeInterface> getChildren();

  List<String> getDependsOn();

  String getNextNode();
}
