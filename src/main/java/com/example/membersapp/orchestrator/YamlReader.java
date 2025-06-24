package com.example.membersapp.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YamlReader {
  private static final Logger LOG = LoggerFactory.getLogger(YamlReader.class);

  public NodeConfig getNodeConfig(String routeName) {
    // Create an ObjectMapper configured for YAML
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    var fileName = "routes/" + routeName + ".yaml";
    // Use try-with-resources to ensure the InputStream is closed
    try (InputStream inputStream =
        YamlReader.class.getClassLoader().getResourceAsStream(fileName)) {
      if (inputStream == null) {
        System.err.println("Error: config.yaml not found in resources directory.");
        throw new RuntimeException("Error: " + fileName + " not found in resources directory.");
      }

      // Read the YAML and map it to our Config POJO
      NodeConfig config = mapper.readValue(inputStream, NodeConfig.class);

      return config;
    } catch (Exception e) {
      LOG.error("Error reading or parsing YAML file: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
