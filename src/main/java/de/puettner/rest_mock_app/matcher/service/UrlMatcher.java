package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlMatcher implements RequestMatcher {
    public boolean matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        if (matcherConfig.getRequest().getUrlRegEx() != null) {
            boolean matched = matcherConfig.getRequest().getUrlRegExMatchPattern().matcher(restRequest.getUrl()).matches();
            log.debug("Matching '" + matcherConfig.getRequest().getUrlRegEx() + "' with '" + restRequest.getUrl() + "' ? " + matched);
            return matched;
        }
        return false;
    }

    public String getCheckedElementName() {
        return "url";
    }
}
