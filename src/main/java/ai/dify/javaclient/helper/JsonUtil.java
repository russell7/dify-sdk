package ai.dify.javaclient.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for JSON processing operations.
 * <p>
 * Provides configured Jackson ObjectMapper instances for serialization and deserialization
 * of JSON data with standardized settings.
 * </p>
 */
public class JsonUtil {

  /**
   * Builds and configures an ObjectMapper instance with standard settings.
   * <p>
   * Configuration:
   * <ul>
   *   <li>Excludes null values from serialization</li>
   *   <li>Ignores unknown properties during deserialization</li>
   * </ul>
   * </p>
   *
   * @return A configured ObjectMapper instance ready for use
   */
  public static ObjectMapper buildMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

}
