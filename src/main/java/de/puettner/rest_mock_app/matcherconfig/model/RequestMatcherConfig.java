package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMatcherConfig {

    /** Null is ok, but not empty */
    private ElementValueExpression method;
    /** Null is ok, but not empty */
    private ElementValueExpression url;
    /** Null is ok, but not empty */
    private HeaderMatcherConfig header;
    /** Null is ok, but not empty */
    private ElementValueExpression body;

}
