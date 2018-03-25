package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BodyMatcher implements RequestMatcher {

    public boolean matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        // *** Check Body by contains ***
        if (matcherConfig.getRequest().getBodyContains() != null && restRequest.getBody().isPresent()) {
            if (restRequest.getBody().get().contains(matcherConfig.getRequest().getBodyContains())) {
                return true;
            }
        }
        // *** Check Body by RegEx ***
        // Regular Expressions werden nur angewendet wenn sie nicht null sind und ihr Prüfwert ebenfalls ungleich null ist
        if (matcherConfig.getRequest().getBodyRegEx() != null && restRequest.getBody().isPresent()) {
            boolean matched = matcherConfig.getRequest().getBodyRegExMatchPattern().matcher(restRequest.getBody().get()).matches();
            log.debug("Matching '" + matcherConfig.getRequest().getBodyRegEx() + "' with '" + restRequest.getBody().get() + "' ? " + matched);
            return matched;
        }
        return false;
    }
    public String getCheckedElementName() {
        return "bodyRegEx|bodyContains";
    }
}
