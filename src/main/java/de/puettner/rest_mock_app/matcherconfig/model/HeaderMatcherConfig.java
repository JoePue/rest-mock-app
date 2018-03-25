package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class HeaderMatcherConfig {
    private String name;
    private String regex;

}
