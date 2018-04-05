package de.puettner.rest_mock_app.matcherconfig.model;

import javax.annotation.Nullable;

import org.springframework.http.MediaType;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString
public class ResponseConfiguration {

    private int statusCode;
    @Nullable
    private ResponseValueExpression body;
    @Nullable
    private MediaType contentType;
}
