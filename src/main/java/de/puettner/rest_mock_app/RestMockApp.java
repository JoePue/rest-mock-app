package de.puettner.rest_mock_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RestMockApp {

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            log.info("[Argument] " + arg);
        }
        SpringApplication.run(RestMockApp.class, args);
    }


}