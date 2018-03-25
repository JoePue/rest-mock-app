package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import de.puettner.rest_mock_app.matcherconfig.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestMatcherServiceTest {

    MatcherConfigurationReader matcherConfigurationReader;

    RequestMatcherService sut;

    private MatcherConfiguration matcherConfiguration = new MatcherConfiguration();
    private RestRequest request;
    private RequestMatcherConfig requestConfig;
    private ResponseMatcherConfig responseConfig;
    private UUID uuid;

    @Before
    public void before(){
        uuid = UUID.randomUUID();
        this.request = RestRequest.builder().method(RequestMethod.POST).body(Optional.of("abc body_" + uuid + " def")).url("/url_" + uuid).build();
        requestConfig = RequestMatcherConfig.builder().build();
        matcherConfiguration.setRequest(requestConfig);
        responseConfig = ResponseMatcherConfig.builder().body(new ResponseElementValueExpression(uuid.toString())).build();
        matcherConfiguration.setResponse(responseConfig);
        this.matcherConfigurationReader = Mockito.mock(MatcherConfigurationReader.class);
        sut = new RequestMatcherService(this.matcherConfigurationReader);
        assertThat(sut).isNotNull();
    }

    @Test
    public void findDefaultResponse() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList());
        RestRequest request = RestRequest.builder().build();

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getStatusCode()).isEqualTo(500);
        assertThat(actualResult.getBody()).isNotNull();
        assertThat(actualResult.getBody().getContent()).isEqualTo("No suitable matcher found.");
    }

    @Test
    public void findResponseByHttpMethod() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList(matcherConfiguration));
        requestConfig.setMethod(ElementValueExpression.buildByString(RequestMethod.POST.toString()));

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfig);
    }

    @Test
    public void findResponseByUrl() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList(matcherConfiguration));
        requestConfig.setUrl(new ElementValueExpression(request.getUrl()));
        requestConfig.getUrl().matches(request.getUrl());

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfig);
    }

    @Test
    public void findResponseByBodyContains() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList(matcherConfiguration));
        requestConfig.setBody(new ElementValueExpression(request.getBody().get()));

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfig);
    }

    @Test
    public void findResponseByBodyRegEx() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList(matcherConfiguration));
        requestConfig.setBody(ElementValueExpression.buildByRegEx(".*" + uuid.toString() + ".*"));

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfig);
    }
}