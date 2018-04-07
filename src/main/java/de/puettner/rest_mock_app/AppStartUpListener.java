package de.puettner.rest_mock_app;

import de.puettner.rest_mock_app.matcherconfig.AppConfigurationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AppConfigurationBuilder reader;

    @Autowired
    public AppStartUpListener(AppConfigurationBuilder reader) {
        this.reader = reader;
    }

    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("onApplicationEvent");
        reader.build();
    }
}
