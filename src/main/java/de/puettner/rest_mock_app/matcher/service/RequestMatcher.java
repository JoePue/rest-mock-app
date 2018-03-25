package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;

import java.util.Optional;

public interface RequestMatcher {
    Optional<Boolean> matches(RestRequest restRequest, MatcherConfiguration matcherConfig);
    default String getCheckedElementName() {
        return "**MissingMatcherName**";
    }
}
