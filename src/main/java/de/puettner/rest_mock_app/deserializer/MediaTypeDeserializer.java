package de.puettner.rest_mock_app.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;

@Slf4j
public class MediaTypeDeserializer extends StdDeserializer<MediaType> {

    public MediaTypeDeserializer(Class<MediaType> vc) {
        super(vc);
    }

    public MediaTypeDeserializer() {
        this(null);
    }

    @Override
    public MediaType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText();
        return getMediaType(value);
    }

    public static MediaType getMediaType(String value) {
        if (value != null || !value.isEmpty()) {
            try {
                MediaType result = MediaType.parseMediaType(value);
                return result;
            } catch (Exception e) {
                log.error("Invalid Content-Type : " + e.getMessage());
            }
        }
        return MediaType.TEXT_PLAIN;
    }
}
