package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.List;

/**
 * @author Simon
 */
public class ApiErrorDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken token = jp.getCurrentToken();
        switch (token) {
            case START_OBJECT:
                return jp.readValueAs(ApiErrors.class);

            case START_ARRAY:
                TypeReference ref = new TypeReference<List<String>>() {};
                return jp.readValueAs(ref);

            case NOT_AVAILABLE:
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
            case VALUE_TRUE:
            case VALUE_FALSE:
            case VALUE_NULL:
            default:
                throw new JsonMappingException("Can not deserialize instance of " + ApiErrors.class.getName() + " out of " + token);
        }
    }
}
