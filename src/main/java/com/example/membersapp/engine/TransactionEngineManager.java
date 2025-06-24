package com.example.membersapp.engine;

import com.example.membersapp.orchestrator.NodeConfig;
import com.example.membersapp.orchestrator.YamlReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TransactionEngineManager {
  private static final Logger LOG = LoggerFactory.getLogger(TransactionEngineManager.class);
  private final YamlReader yamlReader = new YamlReader();
  @Autowired private ApplicationContext applicationContext;

  private final Map<String, NodeConfig> nodeConfigMaps = new HashMap<>();

  public TransactionEngine getTransactionEngine(String source) throws ClassNotFoundException {
    var transactionEngine = applicationContext.getBean(TransactionEngine.class);
    var nodeConfig = yamlReader.getNodeConfig(source);
    if (nodeConfig.getNodes() != null && !nodeConfig.getNodes().isEmpty()) {
      var dependsOnMap = getDependsOnMap(nodeConfig);
      for (var node : nodeConfig.getNodes()) {
        var parentName = node.keySet().iterator().next();
        var details = node.get(parentName);
        var childNodes = details.getChildren();
        if (childNodes == null) {
          childNodes = List.of();
        }
        for (var child : childNodes) {
          var dependsList = dependsOnMap.get(child);
          if (dependsList == null) {
            dependsList = List.of();
          }
          var className = details.getClassName();
          Class<?> loadedClass = Class.forName(className);
          transactionEngine.add(parentName, child, dependsList, loadedClass);
        }
      }
    }
    LOG.info("transaction engine: {}", transactionEngine);
    return transactionEngine;
  }

  private NodeConfig getNodeConfig(String source) throws ClassNotFoundException {
    return nodeConfigMaps.computeIfAbsent(source, yamlReader::getNodeConfig);
  }

  private Map<String, List<String>> getDependsOnMap(NodeConfig nodeConfig) {
    var map = new HashMap<String, List<String>>();
    for (var node : nodeConfig.getNodes()) {
      var nodeName = node.keySet().iterator().next();
      var details = node.get(nodeName);
      var dependsOnList = details.getDependsOn();
      if (dependsOnList != null) {
        map.put(nodeName, dependsOnList);
      }
    }
    return map;
  }
}
