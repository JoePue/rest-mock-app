package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.TestConstants;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AppConfigurationBuilderTest {

    @Test
    public void initTest() {
        AppConfigurationBuilder builder = new AppConfigurationBuilder(TestConstants.CONFIG_FILENAME, TestConstants.TEST_RESPONSES_DIR);
        List<AppConfiguration> actual = builder.build();
        assertNotNull(actual);
        assertEquals(0, actual.stream().filter(c -> c.getResponse().getContentType() == null).count());
        assertEquals(1, actual.stream().filter(c -> MediaType.APPLICATION_JSON.equals(c.getResponse().getContentType())).count());
    }
}