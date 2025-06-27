# generic-processor

# Overview
The generic processor will receive an incoming REST request from the client. It converts the
request(Java POJO) to a hierarchical map which is passed on to a tree of processing nodes.
Each processing node runs asynchronously. The chain of execution will follow the path from the
input through the processing tree until the output node is reached which returns the response to
the calling client. When a node has multiple child nodes, it will decide via business logic what is
the next node to be executed. It can only execute one node out of all the child nodes. If a given
processing node is dependent on the results of previous nodes it will wait until the previous
nodes have completed before it begins processing.
Configuration is done via yaml files. Each yaml represents one client type with a unique input
and output. When the app receives the request, the app will determine which input/output tree
to use and app will traverse the decision trees until it reaches the output.

![Diagram](images\GenericProcessor.jpg)

# Testing
Testing is done using WireMock to simulate a backend connection. In the test processing nodes
have a test latency of 300 ms. The single tests ensures that all processing nodes executed
successfully. The performance test created 100 concurrent threads which completed in 4.924
secs giving a pass through of about 20 transactions/sec or 1.75 million transactions daily.

# Metrics
Metrics and Graphs are done with open source libraries
- Prometheus: For storing and querying time-series data.
- Grafana: For graphing of data