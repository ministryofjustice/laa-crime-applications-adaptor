package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class JsonUtils {

    public static String objectToJson(Object object) {
        ObjectMapper mapper = getObjectMapper();

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String message = "Unable to serialise [%s] to a JSON String".formatted(object);
            throw new RuntimeException(message);
        }
    }

    public static <T> T jsonToObject(String jsonStr, Class<T> clz) throws JsonProcessingException {
        ObjectMapper mapper = getObjectMapper();
        T returnValue = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
            returnValue = mapper.readValue(jsonStr, clz);
        }
        return returnValue;
    }

    @NotNull
    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper( );
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
