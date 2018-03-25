package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class MethodMatcher implements RequestMatcher {
    public Optional<Boolean> matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        if (matcherConfig.getRequest().getMethod() != null) {
            if (restRequest.getMethod().equals(matcherConfig.getRequest().getMethod())) {
                return Optional.of(true);
            }
            return Optional.of(false);
        }
        return Optional.empty();
    }
    public String getCheckedElementName() {
        return "method";
    }
}
