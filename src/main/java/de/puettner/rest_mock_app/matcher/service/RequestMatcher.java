package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;

public interface RequestMatcher {
    boolean matches(RestRequest restRequest, MatcherConfiguration matcherConfig);
    default String getCheckedElementName() {
        return "**MissingMatcherName**";
    }
}
