package uk.gov.justice.laa.crime.applications.adaptor.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaValidationUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  // Reuse a factory instance
  private static final JsonSchemaFactory SCHEMA_FACTORY =
      JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

  /**
   * @param schemaPathOnClasspath e.g.
   *     "schemas/criminalapplicationsdatastore/maat_application_external.json"
   */
  public static void assertMatchesSchema(String schemaPathOnClasspath, String jsonBody)
      throws IOException {

    // Load schema from classpath using SchemaLocation (gives it a proper base URI)
    JsonSchema schema =
        SCHEMA_FACTORY.getSchema(SchemaLocation.of("classpath:" + schemaPathOnClasspath));

    // Parse the JSON instance
    JsonNode instanceNode = OBJECT_MAPPER.readTree(jsonBody);

    // Validate
    Set<ValidationMessage> errors = schema.validate(instanceNode);

    if (!errors.isEmpty()) {
      String message =
          errors.stream().map(ValidationMessage::getMessage).collect(Collectors.joining("\n"));
      throw new AssertionError("Schema validation failed:\n" + message);
    }
  }
}
