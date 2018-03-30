package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.ElementValueExpression;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestMatcherConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class HeaderMatcher implements RequestMatcher {

    public Optional<Boolean> matches(RestRequest restRequest, MatcherConfiguration matcherConfig) {
        RequestMatcherConfig requestConfig = matcherConfig.getRequest();
        if (requestConfig.getHeader() != null && restRequest.getHeader().isPresent()) {
            ElementValueExpression name = requestConfig.getHeader().getName();
            ElementValueExpression value = requestConfig.getHeader().getValue();

            if (name != null && value != null) {
                Map<String, String> headerMap = restRequest.getHeader().get();
                for (String headerName : headerMap.keySet()) {
                    if (name.matches(headerName) && value.matches(headerMap.get(headerName))) {
                        return Optional.of(true);
                    }
                }
            }
            return Optional.of(false);
        }

        // TODO Implementation
        return Optional.empty();
    }

    public String getCheckedElementName() {
        return "header";
    }
}
