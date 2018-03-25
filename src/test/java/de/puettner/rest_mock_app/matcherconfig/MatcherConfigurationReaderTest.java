package de.puettner.rest_mock_app.matcherconfig;

import org.junit.Test;

public class MatcherConfigurationReaderTest {

    String basedir = "./responses";
    String configFilename = "matcher-config.json";

    @Test
    public void initTest() {
        MatcherConfigurationReader reader = new MatcherConfigurationReader(basedir, configFilename);
    }
}