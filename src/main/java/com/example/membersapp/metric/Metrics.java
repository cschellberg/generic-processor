package com.example.membersapp.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

public class Metrics {

  private final MeterRegistry meterRegistry;
  private Counter successCounter;
  private Counter failureCounter;

  public Metrics(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  public void addSuccess(String nodeName) {
    var successCounter =
        meterRegistry.counter(
            "success_counter", // Metric name
            Tags.of("node", nodeName));
    successCounter.increment();
  }

  public void addFalures(String nodeName) {
    var failureCounter =
        meterRegistry.counter(
            "failer_counter", // Metric name
            Tags.of("node", nodeName));
    failureCounter.increment();
  }

  public Timer getTimer(String nodeName) {
    return meterRegistry.timer("node_latency", Tags.of("node", nodeName));
  }
}
