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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
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
    public void before() {
        uuid = UUID.randomUUID();
        this.request = RestRequest.builder().method(RequestMethod.POST).body(Optional.of("abc body_" + uuid + " def")).url("/url_" +
                uuid).header(Optional.empty()).build();
        requestConfig = RequestMatcherConfig.builder().build();
        matcherConfiguration.setRequest(requestConfig);
        responseConfig = ResponseMatcherConfig.builder().body(new ResponseElementValueExpression(uuid.toString())).build();
        matcherConfiguration.setResponse(responseConfig);
        this.matcherConfigurationReader = Mockito.mock(MatcherConfigurationReader.class);
        sut = new RequestMatcherService(this.matcherConfigurationReader);
        assertThat(sut).isNotNull();
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList(matcherConfiguration));
    }

    @Test
    public void findDefaultResponse() {
        when(matcherConfigurationReader.createConfig()).thenReturn(Arrays.asList());
        RestRequest request = RestRequest.builder().header(Optional.empty()).method(RequestMethod.GET).url("testUrl").build();

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getStatusCode()).isEqualTo(500);
        assertThat(actualResult.getBody()).isNotNull();
        assertThat(actualResult.getBody().getValue()).isEqualTo("No suitable matcher found.");
    }

    @Test
    public void findResponseByHttpMethod() {
        ElementValueExpression method = ElementValueExpression.buildByString(RequestMethod.POST.toString());
        requestConfig.setMethod(method);

        assertTrue(method.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByUrl() {
        ElementValueExpression url = new ElementValueExpression(request.getUrl());
        requestConfig.setUrl(url);
        requestConfig.getUrl().matches(request.getUrl());

        assertTrue(url.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByBodyContains() {
        ElementValueExpression body = new ElementValueExpression(request.getBody().get());
        requestConfig.setBody(body);

        assertTrue(body.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByBodyRegEx() {
        ElementValueExpression body = ElementValueExpression.buildByRegEx(".*" + uuid.toString() + ".*");
        requestConfig.setBody(body);

        assertTrue(body.isRegularExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByHeaderNameAndHeaderValue() {
        String testHeader = "myHeader_" + uuid;
        String operator = "";
        ElementValueExpression name = new ElementValueExpression(operator + testHeader);
        ElementValueExpression value = new ElementValueExpression(operator + "myHeaderValue_" + uuid);
        Map<String, String> headers = new HashMap<>();
        headers.put(name.getValue(), value.getValue());
        this.request.setHeader(Optional.of(headers));
        HeaderMatcherConfig header = new HeaderMatcherConfig(name, value);
        requestConfig.setHeader(header);

        assertTrue(name.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByHeaderNameAndHeaderValueByRegEx() {
        String testHeader = "myHeader_" + uuid;
        String operator = "regex::";
        ElementValueExpression name = new ElementValueExpression(operator + testHeader);
        name.init();
        ElementValueExpression value = new ElementValueExpression(operator + "myHeaderValue_" + uuid);
        value.init();
        Map<String, String> headers = new HashMap<>();
        headers.put(name.getValue(), value.getValue());
        this.request.setHeader(Optional.of(headers));
        HeaderMatcherConfig header = new HeaderMatcherConfig(name, value);
        requestConfig.setHeader(header);

        assertTrue(value.isRegularExpression());
        findResponseAndAssert();
    }

    private ResponseMatcherConfig findResponseAndAssert() {
        // when
        ResponseMatcherConfig actualResult = sut.findResponse(request);
        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfig);
        return actualResult;
    }
}