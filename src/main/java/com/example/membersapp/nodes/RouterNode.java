package com.example.membersapp.nodes;

import static com.example.membersapp.model.Message.OPERATION;

import com.example.membersapp.metric.Metrics;
import com.example.membersapp.model.Message;

public class RouterNode extends TreeNode {

  public RouterNode(String name, Metrics metrics) {
    super(name, metrics);
  }

  protected void setNextNode(Message message) {
    nextNode = (String) message.getScratchPadMap().get(OPERATION);
  }
}
