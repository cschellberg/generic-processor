package com.example.membersapp.nodes;

import com.example.membersapp.backend.BackendConnector;
import com.example.membersapp.model.Metric;
import com.example.membersapp.model.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.membersapp.Constants.RESPONSE_CODE;
import static com.example.membersapp.Constants.RESPONSE_DESCRIPTION;

public class AuthorizerNode extends TreeNode {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorizerNode.class);

  public AuthorizerNode(BackendConnector backendConnector) {
    super(backendConnector);
  }

  @Override
  protected void executeBody(
      Map<String, Object> workingMap,
      CompletableFuture<TransactionResponse> future,
      Metric metric) {
    backendConnector
            .getResponse()
            .subscribe(
                    response -> {
                      LOG.info("Node {} is executing with workingMap {}", name, workingMap);
                      metric.endWithSuccess();
                        ((Map)workingMap.get("response")).put(RESPONSE_CODE,"000");
                        ((Map)workingMap.get("response")).put(RESPONSE_DESCRIPTION,"Approved");
                      future.complete(new TransactionResponse(response.getResponse()));
                    },
                    error -> {
                      LOG.error("Node {} failed execution because {}", name, error.toString());
                      metric.endWithFailure();
                        ((Map)workingMap.get("response")).put(RESPONSE_CODE,"909");
                        ((Map)workingMap.get("response")).put(RESPONSE_DESCRIPTION,"Declined");
                        future.completeExceptionally(error);
                    });
  }
}
