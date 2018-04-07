package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.TestConstants;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseValueExpression;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static de.puettner.rest_mock_app.TestConstants.TESTFILE_XML;
import static de.puettner.rest_mock_app.TestConstants.TEST_RESPONSES_DIR;
import static org.junit.Assert.assertEquals;

public class AppConfigurationInitializerTest {

    private AppConfigurationInitializer initializer = new AppConfigurationInitializer(TEST_RESPONSES_DIR);

    @Test
    public void init() {
        List<AppConfiguration> configurations = new ArrayList<>();
        AppConfiguration config = new AppConfiguration();
        config.setRequest(RequestConfiguration.builder().build());
        ResponseConfiguration response = ResponseConfiguration.builder().body(new ResponseValueExpression("file::" + TESTFILE_XML)).statusCode(200).build();
        config.setResponse(response);
        configurations.add(config);
        // when
        initializer.init(configurations);
        // then
        assertEquals(MediaType.APPLICATION_XML, response.getContentType());
    }
}