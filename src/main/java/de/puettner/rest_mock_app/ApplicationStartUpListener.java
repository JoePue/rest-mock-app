package de.puettner.rest_mock_app;

import de.puettner.rest_mock_app.matcherconfig.MatcherConfigurationReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationStartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    private final MatcherConfigurationReader reader;

    @Autowired
    public ApplicationStartUpListener(MatcherConfigurationReader reader) {
        this.reader = reader;
    }

    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("onApplicationEvent");
        reader.createConfig();
    }
}
