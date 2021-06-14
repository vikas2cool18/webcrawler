package com.udacity.webcrawler.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A static utility class that loads a JSON configuration file.
 */
public final class ConfigurationLoader {

  private final Path path;

  /**
   * Create a {@link ConfigurationLoader} that loads configuration from the given {@link Path}.
   */
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }

  /**
   * Loads configuration from this {@link ConfigurationLoader}'s path
   *
   * @return the loaded {@link CrawlerConfiguration}.
   */
  public CrawlerConfiguration load() throws IOException {
    // TODO: Fill in this method.
    try (Reader reader = Files.newBufferedReader(path)) {
      return read(reader);
    } catch (Exception e) {
      System.err.println("Exception at load(), " + e.getMessage());
    }
    return null;
  }
  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */
  public static CrawlerConfiguration read(Reader reader) throws IOException {
    // This is here to get rid of the unused variable warning.
    Objects.requireNonNull(reader);
    // TODO: Fill in this method
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
      CrawlerConfiguration.Builder crawlerConfigurationBuilder = objectMapper.readValue(reader, CrawlerConfiguration.Builder.class);
      CrawlerConfiguration crawlerConfiguration = crawlerConfigurationBuilder.build();
      return crawlerConfiguration;
    } catch (Exception e) {
      System.err.println("Exception at read(), " + e.getLocalizedMessage());
    }
    return null;
  }
}
