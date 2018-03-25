package de.puettner.rest_mock_app.matcher.service;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseMatcherConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestMatcherServiceTest {

    @Mock
    MatcherConfigurationReader matcherConfigurationReader;

    @InjectMocks
    RequestMatcherService sut;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        assertThat(sut).isNotNull();
    }

    @Test
    public void findResponseAssertDefaultResponse() {
        List<MatcherConfiguration> config = null;
        when(matcherConfigurationReader.createConfig()).thenReturn(config);
        RestRequest request = RestRequest.builder().build();

        ResponseMatcherConfig actualResult = sut.findResponse(request);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getStatusCode()).isEqualTo(500);
        assertThat(actualResult.getFilename()).isNotNull();
        assertThat(actualResult.getFile()).isNull();
    }

    @Test
    public void findResponse() {

    }
}