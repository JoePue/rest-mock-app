package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.model.ValueExpression;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class HeaderMatcher implements RequestMatcher {

    public Optional<Boolean> matches(RestRequest restRequest, AppConfiguration matcherConfig) {
        RequestConfiguration requestConfig = matcherConfig.getRequest();
        if (requestConfig.getHeader() != null && restRequest.getHeader().isPresent()) {
            ValueExpression name = requestConfig.getHeader().getName();
            ValueExpression value = requestConfig.getHeader().getValue();

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
