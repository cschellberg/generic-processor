package com.example.membersapp.nodes;

import static com.example.membersapp.model.Message.*;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.model.Message;
import com.example.membersapp.model.Metric;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PurchaseNode extends TreeNode {
  private static final Logger LOG = LoggerFactory.getLogger(PurchaseNode.class);

  public PurchaseNode(BackendConnector backendConnector) {
    super(backendConnector);
  }

  @Override
  protected void executeBody(Message message, CompletableFuture<Void> future, Metric metric) {
    message.addToScratchPad(OPERATION, this.getName());
    backendConnector
        .getResponse()
        .subscribe(
            response -> {
              LOG.info("Node {} is executing with workingMap {}", name, message);
              metric.endWithSuccess();
              message.addToScratchPad(RESPONSE_CODE, "000");
              message.addToScratchPad(RESPONSE_DESCRIPTION, "Approved");
              future.complete(null);
            },
            error -> {
              LOG.error("Node {} failed execution because {}", name, error.toString());
              metric.endWithFailure();
              message.addToScratchPad(RESPONSE_CODE, "909");
              message.addToScratchPad(RESPONSE_DESCRIPTION, "Declined");
              future.completeExceptionally(error);
            });
  }
}
