package com.example.membersapp.nodes;

import static com.example.membersapp.model.Message.*;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.metric.Metrics;
import com.example.membersapp.model.Message;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizerNode extends TreeNode {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorizerNode.class);

  public AuthorizerNode(BackendConnector backendConnector, Metrics metrics) {
    super(backendConnector, metrics);
  }

  @Override
  protected void executeBody(Message message, CompletableFuture<Void> future) {
    backendConnector
        .getResponse()
        .subscribe(
            response -> {
              LOG.info("Node {} is executing with workingMap {}", name, message);
              metrics.addSuccess(this.name);
              message.addToScratchPad(RESPONSE_CODE, "000");
              message.addToScratchPad(RESPONSE_DESCRIPTION, "Approved");
              future.complete(null);
            },
            error -> {
              LOG.error("Node {} failed execution because {}", name, error.toString());
              metrics.addFalures(this.name);
              message.addToScratchPad(RESPONSE_CODE, "909");
              message.addToScratchPad(RESPONSE_DESCRIPTION, "Declined");
              future.completeExceptionally(error);
            });
  }
}
