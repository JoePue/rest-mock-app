package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString
public class ResponseConfiguration {

    private int statusCode;
    private ResponseValueExpression body;

}
