package com.example.membersapp.engine;

import com.example.membersapp.engine.exception.TransactionActionEngineException;
import com.example.membersapp.model.Message;
import com.example.membersapp.model.Transaction;
import com.example.membersapp.nodes.RootNode;
import com.example.membersapp.nodes.TreeNodeInterface;
import com.example.membersapp.orchestrator.NodeDetails;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TransactionEngine {
  private static AtomicInteger idCounter = new AtomicInteger(0);
  private final TreeNodeInterface root;
  private static final Logger LOG = LoggerFactory.getLogger(TransactionEngine.class);
  public static final String INPUT = "input";
  public static final String OUTPUT = "output";
  public static final String ROOT = "root";
  @Autowired private ApplicationContext applicationContext;

  public TransactionEngine() {
    root = new RootNode();
  }

  public void addToRoot(Map<String, NodeDetails> firstNode) {
    var firstNodeName = firstNode.keySet().stream().findFirst().get();
    var firstNodeDetails = firstNode.get(firstNodeName);
    var treeNode = (TreeNodeInterface) applicationContext.getBean(firstNodeDetails.getBeanName());
    treeNode.setName(firstNodeName);
    treeNode.setDependsOn(List.of(ROOT));
    root.addChild(treeNode);
  }

  public void add(String parentName, String childName, List<String> dependsOn, String beanName) {
    var treeNode = findNode(childName, root);
    if (treeNode == null) {
      treeNode = (TreeNodeInterface) applicationContext.getBean(beanName);
      treeNode.setName(childName);
      treeNode.setDependsOn(dependsOn);
    }
    var node = findNode(parentName, root);
    if (node != null) {
      node.addChild(treeNode);
    } else {
      LOG.warn(parentName + " node has not been found");
    }
  }

  private TreeNodeInterface findNode(String parentName, TreeNodeInterface treeNode) {
    if (treeNode.getName().equals(parentName)) {
      return treeNode;
    }
    for (TreeNodeInterface child : treeNode.getChildren()) {
      // Recursively call findNode on each child
      TreeNodeInterface foundNode = findNode(parentName, child);
      if (foundNode != null) {
        return foundNode;
      }
    }
    return null;
  }

  public Message execute(Transaction transaction) throws TransactionActionEngineException {
    transaction.setTransactionDate(new Date());
    transaction.setTransactionId(getTransactionId());
    var message = new Message(transaction);
    try {
      return execute(message);
    } catch (Exception ex) {
      throw new TransactionActionEngineException(message);
    }
  }

  public Message execute(Message message)
      throws ExecutionException, InterruptedException, TimeoutException {
    var completableFutureMap = new HashMap<String, CompletableFuture<Void>>();
    this.execute(completableFutureMap, List.of(root), message);
    CompletableFuture.allOf(completableFutureMap.values().toArray(new CompletableFuture[0]))
        .get(30, TimeUnit.SECONDS);
    return message;
  }

  private void execute(
      Map<String, CompletableFuture<Void>> completableFutureMap,
      List<TreeNodeInterface> treeNodes,
      Message message) {
    var childNodes = new ArrayList<TreeNodeInterface>();
    for (TreeNodeInterface treeNode : treeNodes) {
      if (!treeNode.getDependsOn().isEmpty()) {
        var dependsOnList = new ArrayList<CompletableFuture<Void>>();
        for (String name : treeNode.getDependsOn()) {
          if (completableFutureMap.containsKey(name)) {
            dependsOnList.add(completableFutureMap.get(name));
          }
        }
        var dependsOnArray = dependsOnList.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(dependsOnArray).join();
        LOG.info("Depends on: {} treeNode {} waiting...", treeNode.getName(), dependsOnList);
      }
      completableFutureMap.put(treeNode.getName(), treeNode.execute(message));
      var selectNodes =
          treeNode.getChildren().stream()
              .filter(
                  treeNodeInterface -> {
                    var nextNode = treeNode.getNextNode();
                    return (nextNode != null && nextNode.equals(treeNodeInterface.getName()));
                  })
              .toList();
      childNodes.addAll(selectNodes);
    }
    if (!childNodes.isEmpty()) {
      execute(completableFutureMap, childNodes, message);
    }
  }

  private String getTransactionId() {
    var idNum = idCounter.getAndIncrement();
    if (idNum > 999999) {
      idNum = 0;
      idCounter.set(0);
    }
    return String.format("%06d", idNum);
  }

  @Override
  public String toString() {
    return "TransactionEngine{" + root + '}';
  }
}
