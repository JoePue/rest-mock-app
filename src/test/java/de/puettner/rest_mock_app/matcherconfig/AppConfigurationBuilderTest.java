package de.puettner.rest_mock_app.matcherconfig;

import org.junit.Test;

public class AppConfigurationBuilderTest {

    private final String basedir = "./src/main/resources/responses";
    private final String configFilename = "matcher-config.json";

    @Test
    public void initTest() {
        AppConfigurationBuilder builder = new AppConfigurationBuilder(configFilename, basedir);
        builder.build();
    }
}