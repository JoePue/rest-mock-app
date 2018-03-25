package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class UrlMatcher implements RequestMatcher {
    public Optional<Boolean> matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        if (matcherConfig.getRequest().getUrlRegEx() != null) {
            boolean matched = matcherConfig.getRequest().getUrlRegExMatchPattern().matcher(restRequest.getUrl()).matches();
            log.debug("Matching '" + matcherConfig.getRequest().getUrlRegEx() + "' with '" + restRequest.getUrl() + "' ? " + matched);
            return Optional.of(matched);
        }
        return Optional.empty();
    }

    public String getCheckedElementName() {
        return "url";
    }
}
