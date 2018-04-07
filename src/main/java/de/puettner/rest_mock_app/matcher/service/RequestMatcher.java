package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;

import java.util.Optional;

public interface RequestMatcher {
    Optional<Boolean> matches(RestRequest restRequest, AppConfiguration matcherConfig);

    default String getCheckedElementName() {
        return "**MissingMatcherName**";
    }
}
