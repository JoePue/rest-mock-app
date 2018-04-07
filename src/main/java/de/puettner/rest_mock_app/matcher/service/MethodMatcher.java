package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class MethodMatcher implements RequestMatcher {
    public Optional<Boolean> matches(RestRequest restRequest, AppConfiguration matcherConfig) {
        if (matcherConfig.getRequest().getMethod() != null) {
            boolean matched = matcherConfig.getRequest().getMethod().matches(restRequest.getMethod().toString());
            return Optional.of(matched);
        }
        return Optional.empty();
    }

    public String getCheckedElementName() {
        return "method";
    }
}
