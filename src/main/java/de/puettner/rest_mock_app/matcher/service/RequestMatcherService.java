package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseElementValueExpression;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseMatcherConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RequestMatcherService {

    private final ResponseMatcherConfig defaultResponse = new ResponseMatcherConfig(500,
            new ResponseElementValueExpression("No suitable matcher found."));
    private final MatcherConfigurationReader matcherConfigurationReader;

    /**
     * C'tor
     */
    @Autowired
    public RequestMatcherService(MatcherConfigurationReader matcherConfigurationReader) {
        this.matcherConfigurationReader = matcherConfigurationReader;
    }

    /**
     * Returns a response that corresponds to a request matcher, that matched to http request
     *
     * @return Matching response for the request
     */
    public ResponseMatcherConfig findResponse(RestRequest restRequest) {
        log.trace(MessageFormat.format("findResponse() ", restRequest));
        List<MatcherConfiguration> matcherConfigList = matcherConfigurationReader.createConfig();
        List<MatcherConfiguration> matcherConfiguration = matching(restRequest, matcherConfigList, Optional.of(1));

        if (matcherConfiguration.size() == 1) {
            return matcherConfiguration.get(0).getResponse();
        }
        if (matcherConfiguration.size() != 0) {
            log.warn(MessageFormat.format("Multiple ({0}) configurations matched, the first wins", matcherConfiguration.size()));
            return matcherConfiguration.get(0).getResponse();
        }
        log.warn("Found no matching configuration, so a default response will be returned");
        return defaultResponse;
    }

    /**
     * Finds all matchers that matching with the request.
     *
     * @param matcherConfiguration All configured matcher.
     * @param limit                limit result size
     * @return list of positive matchers
     */
    private List<MatcherConfiguration> matching(RestRequest restRequest, List<MatcherConfiguration> matcherConfiguration,
                                                Optional<Integer> limit) {
        List<MatcherConfiguration> positivMatcherList = new ArrayList<>();
        log.trace("matching()");
        RequestMatcher[] fullMatcherList = {new MethodMatcher(), new UrlMatcher(), new HeaderMatcher(), new BodyMatcher()};
        StringBuilder matchResultMessage;
        Boolean isConfigMatching;

        for (MatcherConfiguration matcherConfig : matcherConfiguration) {
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
                if (!positivMatcherList.contains(matcherConfig)) {
                    positivMatcherList.add(matcherConfig);
                    if (matchResultMessage.charAt(matchResultMessage.length() - 1) == ',') {
                        matchResultMessage = matchResultMessage.deleteCharAt(matchResultMessage.length() - 1);
                    }
                    matchResultMessage.append("   =>   Used in response");
                }
            }
            log.info(matchResultMessage.toString());
            if (limit.isPresent() && positivMatcherList.size() >= limit.get()) {
                break;
            }
        }
        return positivMatcherList;
    }

    /**
     * Finds all matching request matcher
     *
     * @return list of matcher
     */
    public MatcherConfiguration[] matchedRules(RestRequest restRequest) {
        List<MatcherConfiguration> matcherConfig = matcherConfigurationReader.createConfig();
        List<MatcherConfiguration> result = matching(restRequest, matcherConfig, Optional.empty());
        return result.toArray(new MatcherConfiguration[result.size()]);
    }

}
