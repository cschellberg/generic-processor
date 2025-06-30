package com.example.membersapp.nodes;

import static com.example.membersapp.engine.TransactionEngine.ROOT;

import com.example.membersapp.model.Message;

public class RootNode extends TreeNode {

  public RootNode() {
    super(ROOT, null);
  }

  @Override
  protected void setNextNode(Message message) {
    nextNode = this.getChildren().getFirst().getName();
  }
}
