package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.deserializer.MediaTypeDeserializer;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import org.springframework.http.MediaType;

import java.util.List;

public class AppConfigurationInitializer {
    private final String responseFileBaseDir;

    public AppConfigurationInitializer(String responseFileBaseDir) {
        this.responseFileBaseDir = responseFileBaseDir;
    }

    public void init(List<AppConfiguration> appConfigurations) {
        for (AppConfiguration appConfiguration : appConfigurations) {
            init(appConfiguration.getRequest());
            init(appConfiguration.getResponse());
        }
    }

    private void init(RequestConfiguration requestConfig) {
        if (requestConfig.getHeader() != null) {
            requestConfig.getHeader().getName().init(responseFileBaseDir, true);
            requestConfig.getHeader().getValue().init();
        }
        if (requestConfig.getUrl() != null) {
            requestConfig.getUrl().init();
        }
        if (requestConfig.getBody() != null) {
            requestConfig.getBody().init(responseFileBaseDir);
        }
        if (requestConfig.getMethod() != null) {
            requestConfig.getMethod().init();
        }
    }

    private void init(ResponseConfiguration responseConfig) {
        responseConfig.getBody().init(responseFileBaseDir);

        if (responseConfig.getContentType() == null) {
            if (responseConfig.getBody().isFileExpression()) {
                String filename = responseConfig.getBody().getExpression().toLowerCase();
                int idx = filename.lastIndexOf('.');
                if (idx > 0 && idx < filename.length()) {
                    filename = filename.substring(idx + 1);
                    responseConfig.setContentType(MediaTypeDeserializer.getMediaType("application/" + filename));
                } else {
                    responseConfig.setContentType(MediaType.TEXT_PLAIN);
                }
            } else {
                responseConfig.setContentType(MediaType.TEXT_PLAIN);
            }
        }
    }
}
