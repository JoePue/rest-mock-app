package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ToString
public class ResponseMatcherConfig {

    private int statusCode;
    private ResponseElementValueExpression body;

}
