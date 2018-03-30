package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BodyMatcher implements RequestMatcher {

    public Optional<Boolean> matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        // *** Check Body by RegEx ***
        // Regular Expressions werden nur angewendet wenn sie nicht null sind und ihr Prüfwert ebenfalls ungleich null ist
        if (matcherConfig.getRequest().getBody() != null && restRequest.getBody().isPresent()) {
            boolean matched = matcherConfig.getRequest().getBody().matches(restRequest.getBody().get());
            log.debug("Matching '" + matcherConfig.getRequest().getBody().getValue() + "' with '" + restRequest.getBody().get() + "' ? " +
                    "" + matched);
            return Optional.of(matched);
        }
        return Optional.empty();
    }

    public String getCheckedElementName() {
        return "body";
    }
}
