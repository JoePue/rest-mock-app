package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.AppConfigurationBuilder;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseValueExpression;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RequestMatcherService {

    private final ResponseConfiguration defaultConfig = new ResponseConfiguration(500,
            new ResponseValueExpression("No suitable matcher found."), MediaType.TEXT_PLAIN);
    private final AppConfigurationBuilder matcherConfigurationReader;

    /**
     * C'tor
     */
    @Autowired
    public RequestMatcherService(AppConfigurationBuilder matcherConfigurationReader) {
        this.matcherConfigurationReader = matcherConfigurationReader;
    }

    /**
     * Returns a response that corresponds to a request matcher, that matched to http request
     *
     * @return Matching response for the request
     */
    public ResponseConfiguration findResponse(RestRequest restRequest) {
        log.trace(MessageFormat.format("findResponse() ", restRequest));
        List<AppConfiguration> matcherConfigList = matcherConfigurationReader.build();
        List<AppConfiguration> appConfiguration = matching(restRequest, matcherConfigList, Optional.of(1));

        if (appConfiguration.size() == 1) {
            return appConfiguration.get(0).getResponse();
        }
        if (appConfiguration.size() != 0) {
            log.warn(MessageFormat.format("Multiple ({0}) configurations matched, the first wins", appConfiguration.size()));
            return appConfiguration.get(0).getResponse();
        }
        log.warn("Found no matching configuration, so a default response will be returned");
        return defaultConfig;
    }

    /**
     * Finds all matchers that matching with the request.
     *
     * @param matcherConfigs All configured matcher.
     * @param limit                limit result size
     * @return list of positive matchers
     */
    private List<AppConfiguration> matching(RestRequest restRequest, List<AppConfiguration> matcherConfigs,
                                            Optional<Integer> limit) {
        List<AppConfiguration> matchedConfigs = new ArrayList<>();
        log.trace("matching()");
        RequestMatcher[] fullMatcherList = {new MethodMatcher(), new UrlMatcher(), new HeaderMatcher(), new BodyMatcher()};
        StringBuilder matchResultMessage;
        Boolean isConfigMatching;

        for (AppConfiguration matcherConfig : matcherConfigs) {
            isConfigMatching = null;
            matchResultMessage = new StringBuilder("[" + matcherConfig.getName() + "]");
            log.info(matchResultMessage.toString());
            for (RequestMatcher matcher : fullMatcherList) {
                Optional<Boolean> isMatching = matcher.matches(restRequest, matcherConfig);
                if (isMatching.isPresent()) {
                    matchResultMessage.append(" ").append(matcher.getCheckedElementName()).append(": ").append(isMatching.get()).append(",");
                    if (isConfigMatching == null && isMatching.get()) {
                        isConfigMatching = true;
                    }
                    if (isConfigMatching != null && !isMatching.get()) {
                        isConfigMatching = false;
                    }
                }
            }
            if (isConfigMatching != null && isConfigMatching) {
                if (!matchedConfigs.contains(matcherConfig)) {
                    matchedConfigs.add(matcherConfig);
                    if (matchResultMessage.charAt(matchResultMessage.length() - 1) == ',') {
                        matchResultMessage = matchResultMessage.deleteCharAt(matchResultMessage.length() - 1);
                    }
                    matchResultMessage.append("   =>   Used in response");
                }
            }
            log.info(matchResultMessage.toString());
            if (limit.isPresent() && matchedConfigs.size() >= limit.get()) {
                break;
            }
        }
        return matchedConfigs;
    }

    /**
     * Finds all matching request matcher
     *
     * @return list of matcher
     */
    public AppConfiguration[] matchedRules(RestRequest restRequest) {
        List<AppConfiguration> matcherConfig = matcherConfigurationReader.build();
        List<AppConfiguration> result = matching(restRequest, matcherConfig, Optional.empty());
        return result.toArray(new AppConfiguration[result.size()]);
    }

}
