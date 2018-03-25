package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatcherConfiguration {

    private String name;
    private String description;
    private RequestMatcherConfig request;
    private ResponseMatcherConfig response;

}
