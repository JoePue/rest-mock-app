package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MethodMatcher implements RequestMatcher {
    public boolean matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        return matcherConfig.getRequest().getMethod() != null && restRequest.getMethod().equals(matcherConfig.getRequest().getMethod());
    }
    public String getCheckedElementName() {
        return "method";
    }
}
