package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppConfiguration {

    private String name;
    private String description;
    private RequestConfiguration request;
    private ResponseConfiguration response;

}
