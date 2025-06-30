package com.example.membersapp.nodes;

import com.example.membersapp.model.Message;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TreeNodeInterface {

  CompletableFuture<Void> execute(Message message);

  String getName();

  void setName(String name);

  void setDependsOn(List<String> dependsOn);

  void addChild(TreeNodeInterface child);

  List<TreeNodeInterface> getChildren();

  List<String> getDependsOn();

  String getNextNode();
}
