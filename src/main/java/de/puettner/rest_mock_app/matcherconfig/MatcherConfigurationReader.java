package de.puettner.rest_mock_app.matcherconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestMatcherConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MatcherConfigurationReader {

    private final String matchingConfigurationFilename;

    private final String responseFileBaseDir;

    private final ObjectMapper om = new ObjectMapper();

    /**
     * C'tor
     */
    @Autowired
    public MatcherConfigurationReader(@Value("${app.matcher.configuration.file}") String matchingConfigurationFilename,
                                      @Value("${app.response.file.basedir}") String responseFileBaseDir) {
        this.matchingConfigurationFilename = matchingConfigurationFilename;
        this.responseFileBaseDir = responseFileBaseDir;
    }

    /**
     * Konfig erstellen
     *
     * @return
     */
    public List<MatcherConfiguration> createConfig() {
        if (null == this.responseFileBaseDir || responseFileBaseDir.isEmpty()) {
            throw new AppException("responseFileDir is invalid");
        }
        ConfigurationValidator.validateDir(responseFileBaseDir);
        return createMatcherConfiguration();
    }

    private List<MatcherConfiguration> createMatcherConfiguration() {
        File file = new File(responseFileBaseDir + File.separator + matchingConfigurationFilename);
        ConfigurationValidator.validateFile(file);
        List<MatcherConfiguration> matcherConfiguration;
        try {
            matcherConfiguration = om.readValue(file, new TypeReference<List<MatcherConfiguration>>() {});
        } catch (Exception e) {
            throw new AppException("Invalid JSON file : " + file, e);
        }
        if (matcherConfiguration == null) {
            throw new AppException("Invalid JSON : Empty matcher object");
        }
        for (MatcherConfiguration configItem : matcherConfiguration) {
            RequestMatcherConfig requestConfig = configItem.getRequest();
            if (configItem == null) {
                throw new AppException("Invalid JSON : Empty matcher object");
            }
            if (requestConfig == null) {
                throw new AppException("Invalid JSON : Empty request object");
            }
            if (configItem.getResponse() == null) {
                throw new AppException("Invalid JSON : Empty response object");
            }
            if (configItem.getResponse().getBody() == null) {
                throw new AppException("Invalid JSON : Empty response.body");
            }
            if (requestConfig.getHeader() != null && requestConfig.getHeader().getName() == null && requestConfig.getHeader().getValue()== null) {
                throw new AppException("Invalid JSON : At the same time header name and header value can't be null.");
            }

            if (requestConfig.getUrl() != null) {
                requestConfig.getUrl().init(responseFileBaseDir);
            }
            if (requestConfig.getBody() != null) {
                requestConfig.getBody().init(responseFileBaseDir);
            }
            if (requestConfig.getMethod() != null) {
                requestConfig.getMethod().init(responseFileBaseDir);
            }
            configItem.getResponse().getBody().init(responseFileBaseDir);
        }
        new ConfigurationValidator(matcherConfiguration).validate();
        return matcherConfiguration;
    }

}
