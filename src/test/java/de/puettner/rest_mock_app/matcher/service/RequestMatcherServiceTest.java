package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.AppConfigurationBuilder;
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

    AppConfigurationBuilder appConfigurationBuilder;

    RequestMatcherService sut;

    private AppConfiguration appConfiguration = new AppConfiguration();
    private RestRequest request;
    private RequestConfiguration requestConfig;
    private ResponseConfiguration responseConfiguration;
    private UUID uuid;

    @Before
    public void before() {
        uuid = UUID.randomUUID();
        this.request = RestRequest.builder().method(RequestMethod.POST).body(Optional.of("abc body_" + uuid + " def")).url("/url_" +
                uuid).header(Optional.empty()).build();
        requestConfig = RequestConfiguration.builder().build();
        appConfiguration.setRequest(requestConfig);
        responseConfiguration = ResponseConfiguration.builder().body(new ResponseValueExpression(uuid.toString())).build();
        appConfiguration.setResponse(responseConfiguration);
        this.appConfigurationBuilder = Mockito.mock(AppConfigurationBuilder.class);
        sut = new RequestMatcherService(this.appConfigurationBuilder);
        assertThat(sut).isNotNull();
        when(appConfigurationBuilder.build()).thenReturn(Arrays.asList(appConfiguration));
    }

    @Test
    public void findDefaultResponse() {
        when(appConfigurationBuilder.build()).thenReturn(Arrays.asList());
        RestRequest request = RestRequest.builder().header(Optional.empty()).method(RequestMethod.GET).url("testUrl").build();

        ResponseConfiguration actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getStatusCode()).isEqualTo(500);
        assertThat(actualResult.getBody()).isNotNull();
        assertThat(actualResult.getBody().getValue()).isEqualTo("No suitable matcher found.");
    }

    @Test
    public void findResponseByHttpMethod() {
        ValueExpression method = ValueExpression.buildByString(RequestMethod.POST.toString());
        requestConfig.setMethod(method);

        assertTrue(method.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByUrl() {
        ValueExpression url = new ValueExpression(request.getUrl());
        requestConfig.setUrl(url);
        requestConfig.getUrl().matches(request.getUrl());

        assertTrue(url.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByBodyContains() {
        ValueExpression body = new ValueExpression(request.getBody().get());
        requestConfig.setBody(body);

        assertTrue(body.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByBodyRegEx() {
        ValueExpression body = ValueExpression.buildByRegEx(".*" + uuid.toString() + ".*");
        requestConfig.setBody(body);

        assertTrue(body.isRegularExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByHeaderNameAndHeaderValue() {
        String testHeader = "myHeader_" + uuid;
        String operator = "";
        ValueExpression name = new ValueExpression(operator + testHeader);
        ValueExpression value = new ValueExpression(operator + "myHeaderValue_" + uuid);
        Map<String, String> headers = new HashMap<>();
        headers.put(name.getValue(), value.getValue());
        this.request.setHeader(Optional.of(headers));
        HeaderConfiguration header = new HeaderConfiguration(name, value);
        requestConfig.setHeader(header);

        assertTrue(name.isPlainExpression());
        findResponseAndAssert();
    }

    @Test
    public void findResponseByHeaderNameAndHeaderValueByRegEx() {
        String testHeader = "myHeader_" + uuid;
        String operator = "regex::";
        ValueExpression name = new ValueExpression(operator + testHeader);
        name.init();
        ValueExpression value = new ValueExpression(operator + "myHeaderValue_" + uuid);
        value.init();
        Map<String, String> headers = new HashMap<>();
        headers.put(name.getValue(), value.getValue());
        this.request.setHeader(Optional.of(headers));
        HeaderConfiguration header = new HeaderConfiguration(name, value);
        requestConfig.setHeader(header);

        assertTrue(value.isRegularExpression());
        findResponseAndAssert();
    }

    private ResponseConfiguration findResponseAndAssert() {
        // when
        ResponseConfiguration actualResult = sut.findResponse(request);
        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(responseConfiguration);
        return actualResult;
    }
}