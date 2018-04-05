package de.puettner.rest_mock_app.matcherconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class AppConfigurationBuilder {

    private final String matchingConfigurationFilename;

    private final String responseFileBaseDir;

    private final ObjectMapper om = new ObjectMapper();

    /**
     * C'tor
     */
    @Autowired
    public AppConfigurationBuilder(@Value("${app.matcher.configuration.file}") String matchingConfigurationFilename,
                                   @Value("${app.response.file.basedir}") String responseFileBaseDir) {
        this.matchingConfigurationFilename = matchingConfigurationFilename;
        this.responseFileBaseDir = responseFileBaseDir;
    }

    /**
     * Creates a new configuration.
     *
     * @return Loaded configuration
     */
    public List<AppConfiguration> build() {
        if (null == this.responseFileBaseDir || responseFileBaseDir.isEmpty()) {
            throw new AppException("responseFileDir is invalid");
        }
        Validators.validateDir(responseFileBaseDir);
        return createMatcherConfiguration();
    }

    private List<AppConfiguration> createMatcherConfiguration() {
        File file = new File(responseFileBaseDir + File.separator + matchingConfigurationFilename);
        Validators.validateFile(file);
        List<AppConfiguration> appConfiguration;
        try {
            appConfiguration = om.readValue(file, new TypeReference<List<AppConfiguration>>() {});
        } catch (Exception e) {
            throw new AppException("Invalid JSON file : " + file, e);
        }
        if (appConfiguration == null) {
            throw new AppException("Invalid JSON : Empty matcher object");
        }
        for (AppConfiguration configItem : appConfiguration) {
            if (configItem == null) {
                throw new AppException("Invalid JSON : Empty matcher object");
            }
            validateRequestConfig(configItem.getRequest());
            validateResponseConfig(configItem.getResponse());
            initMatcherConfiguration(configItem);
        }
        new AppConfigurationValidator(appConfiguration).validate();
        return appConfiguration;
    }

    private void validateRequestConfig(RequestConfiguration requestConfig) {
        if (requestConfig == null) {
            throw new AppException("Invalid JSON : Empty request object");
        }
        if (requestConfig.getHeader() != null) {
            if (requestConfig.getHeader().getName() == null || requestConfig.getHeader().getValue() == null) {
                throw new AppException("Invalid JSON : Header name and header value must be set.");
            } else {
                requestConfig.getHeader().getName().init(responseFileBaseDir, true);
                requestConfig.getHeader().getValue().init();
            }
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

    private void validateResponseConfig(ResponseConfiguration response) {
        if (response == null) {
            throw new AppException("Invalid JSON : Empty response object");
        }
        if (response.getBody() == null) {
            throw new AppException("Invalid JSON : Empty response.body");
        }
    }

    private void initMatcherConfiguration(AppConfiguration matcherConfig) {
        ResponseConfiguration responseConfig = matcherConfig.getResponse();
        responseConfig.getBody().init(responseFileBaseDir);

        if (responseConfig.getContentType() == null) {
            if (responseConfig.getBody().isFileExpression()) {
                String filename = responseConfig.getBody().getExpression().toLowerCase();
                if (filename.endsWith(".json")) {
                    responseConfig.setContentType(MediaType.APPLICATION_JSON);
                } else {
                    responseConfig.setContentType(MediaType.TEXT_PLAIN);
                }
            } else {
                responseConfig.setContentType(MediaType.TEXT_PLAIN);
            }
        }
    }
}
