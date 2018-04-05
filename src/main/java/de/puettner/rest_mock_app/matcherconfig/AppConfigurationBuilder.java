package de.puettner.rest_mock_app.matcherconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.puettner.rest_mock_app.deserializer.MediaTypeDeserializer;
import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class AppConfigurationBuilder {

    private final String configurationFilename;
    private final String responseFileBaseDir;
    private final AppConfigurationValidator appConfigValidator;
    private final AppConfigurationInitializer appConfigInitializer;
    private final ObjectMapper om = new ObjectMapper();

    /**
     * C'tor
     */
    @Autowired
    public AppConfigurationBuilder(@Value("${app.matcher.configuration.file}") String matchingConfigurationFilename,
                                   @Value("${app.response.file.basedir}") String responseFileBaseDir) {
        this.configurationFilename = matchingConfigurationFilename;
        this.responseFileBaseDir = responseFileBaseDir;
        this.appConfigValidator = new AppConfigurationValidator(responseFileBaseDir);
        this.appConfigInitializer = new AppConfigurationInitializer(responseFileBaseDir);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MediaType.class, new MediaTypeDeserializer());
        om.registerModule(module);
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
        AppConfigurationValidator.validateDir(responseFileBaseDir);
        File file = new File(responseFileBaseDir + File.separator + configurationFilename);
        AppConfigurationValidator.validateFile(file);
        List<AppConfiguration> appConfigurations;
        try {
            appConfigurations = om.readValue(file, new TypeReference<List<AppConfiguration>>() {});
        } catch (Exception e) {
            throw new AppException("Invalid JSON file : {0}", e, file);
        }
        appConfigInitializer.init(appConfigurations);
        return appConfigurations;
    }

}
