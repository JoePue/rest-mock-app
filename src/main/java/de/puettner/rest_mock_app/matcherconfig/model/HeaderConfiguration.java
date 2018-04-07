package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderConfiguration {

    /** Not Null */
    @NotNull
    private ValueExpression name;
    /** Not Null */
    @NotNull
    private ValueExpression value;
}
