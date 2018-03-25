package de.puettner.rest_mock_app.matcher.rest;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcher.service.RequestMatcherService;
import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class MatcherRestController {

	private final MatcherConfigurationReader configReader;
	private final RequestMatcherService matcher;

	@Autowired
	public MatcherRestController(MatcherConfigurationReader matcherConfigurationReader, RequestMatcherService requestMatcher) {
		this.configReader = matcherConfigurationReader;
		this.matcher = requestMatcher;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/matcher/config")
	public @ResponseBody List<MatcherConfiguration> getAll() {
		return configReader.createConfig();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/matching")
	public @ResponseBody List<MatcherConfiguration> matching(@RequestBody String body, HttpServletRequest request) {
		RestRequest restRequest = RestRequest.builder().method(RequestMethod.POST).body(Optional.of(body)).url(request.getRequestURI()).build();

		return Arrays.asList(matcher.matchedRules(restRequest));
	}

}
