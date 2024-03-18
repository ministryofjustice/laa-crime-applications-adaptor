package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class JsonUtils {

  private JsonUtils() {}

  public static String objectToJson(Object object) {
    ObjectMapper mapper = getObjectMapper();

    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      String message = "Unable to serialise [%s] to a JSON String".formatted(object);
      throw new RuntimeException(message, e);
    }
  }

  public static <T> T jsonToObject(String jsonStr, Class<T> clz) {
    ObjectMapper mapper = getObjectMapper();
    T returnValue = null;
    if (StringUtils.isNotEmpty(jsonStr)) {
      try {
        returnValue = mapper.readValue(jsonStr, clz);
      } catch (JsonProcessingException e) {
        String message =
            "Unable to deserialise [%s] into a [%s] encountered error: %s"
                .formatted(jsonStr, clz, e.getMessage());
        throw new RuntimeException(message, e);
      }
    }
    return returnValue;
  }

  @NotNull private static ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper;
  }
}
