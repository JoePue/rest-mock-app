package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderMatcherConfig {
    /** Not Null */
    @NotNull
    private ElementValueExpression name;
    /** Not Null */
    @NotNull
    private ElementValueExpression value;
}
