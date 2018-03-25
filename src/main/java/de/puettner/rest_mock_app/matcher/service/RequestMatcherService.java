package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
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

    private final ResponseMatcherConfig defaultResponse = new ResponseMatcherConfig(500, "No suitable matcher found.", null);
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
        log.debug(MessageFormat.format("findResponse() ", restRequest));
        List<MatcherConfiguration> matcherConfigArray = matcherConfigurationReader.createConfig();
        List<MatcherConfiguration> matcherConfiguration = matching(restRequest, matcherConfigArray, Optional.empty());

        if (matcherConfiguration.size() == 1) {
            return getMatchedResult(matcherConfiguration.get(0));
        }
        if (matcherConfiguration.size() == 0) {
            log.warn("Found no matching configuration");
        } else {
            log.warn("Multiple configurations matched");
        }
        log.warn("No suitable matcher found, so a default response will be returned");
        return defaultResponse;
    }

    /**
     * Finds all matchers that matching with the request.
     * 
     * @param matcherConfiguration
     *            All configured matcher.
     * @param limit
     *            limit result size
     * @return list of positive matchers
     */
    private List<MatcherConfiguration> matching(RestRequest restRequest, List<MatcherConfiguration> matcherConfiguration,
                                                Optional<Integer> limit) {
        List<MatcherConfiguration> positivMatcherList = new ArrayList<>();
        log.info("matching()");
        log.debug("matcherConfiguration.size : " + matcherConfiguration.size());
        RequestMatcher[] fullMatcherList = {new MethodMatcher(), new UrlMatcher(), new HeaderMatcher(), new BodyMatcher()};
        String matchResultMessage;
        for (MatcherConfiguration matcherConfig : matcherConfiguration) {
            matchResultMessage = "[" + matcherConfig.getName() + "]";
            for (RequestMatcher matcher : fullMatcherList) {
                Optional<Boolean> matcherResult = matcher.matches(restRequest, matcherConfig);
                if (matcherResult.isPresent() && matcherResult.get()) {
                    matchResultMessage += " " + matcher.getCheckedElementName() + ": " + matcherResult + ",";
                    if (!positivMatcherList.contains(matcherConfig)) {
                        positivMatcherList.add(matcherConfig);
                    }
                    if (limit.isPresent() && positivMatcherList.size() >= limit.get()) {
                        break;
                    }
                } else {
                    log.debug(matcher.getClass().getSimpleName() + " not matching.");
                }
            }
            log.info(matchResultMessage);
        }
        return positivMatcherList;
    }

    private ResponseMatcherConfig getMatchedResult(MatcherConfiguration matcherConfig) {
        log.info("[matched.details] " + MessageFormat.format("method={0}, urlRegEx={1}, bodyRegExMatch={2}", matcherConfig.getRequest().getMethod(),
                matcherConfig.getRequest().getUrlRegEx(), matcherConfig.getRequest().getBodyRegEx()));
        log.info("[response.file] " + matcherConfig.getResponse().getFilename());
        String fileContent = matcherConfigurationReader.readResponseFile(matcherConfig.getResponse().getFilename());
        return new ResponseMatcherConfig(matcherConfig.getResponse().getStatusCode(), fileContent, null);
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
