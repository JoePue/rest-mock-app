package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderMatcherConfig {
    private ElementValueExpression name;
    private ElementValueExpression value;
}
