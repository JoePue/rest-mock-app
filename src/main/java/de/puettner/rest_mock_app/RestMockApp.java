package de.puettner.rest_mock_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class RestMockApp {

    public static Logger LOG = Logger.getLogger(RestMockApp.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            LOG.info("[Argument] " + arg);
        }
        SpringApplication.run(RestMockApp.class, args);
    }


}