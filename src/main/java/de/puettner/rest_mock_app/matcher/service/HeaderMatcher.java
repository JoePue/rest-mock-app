package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderMatcher implements RequestMatcher {
    public boolean matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        // TODO Implementation
        return false;
    }
    public String getCheckedElementName() {
        return "header";
    }
}
