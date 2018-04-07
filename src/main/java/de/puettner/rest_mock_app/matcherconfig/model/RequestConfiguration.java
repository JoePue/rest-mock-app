package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestConfiguration {

    /** Null is ok, but not empty */
    private ValueExpression method;
    /** Null is ok, but not empty */
    private ValueExpression url;
    /** Null is ok, but not empty */
    private HeaderConfiguration header;
    /** Null is ok, but not empty */
    private ValueExpression body;

}
