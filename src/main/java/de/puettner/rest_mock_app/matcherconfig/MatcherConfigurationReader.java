package de.puettner.rest_mock_app.matcherconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

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
        return createMatcherConfiguration(responseFileBaseDir);
    }

    public String readResponseFile(String filename) {
        File file = new File(responseFileBaseDir + File.separatorChar + filename);
        StringBuilder fileContent = new StringBuilder();
        readFile(file).forEach(s -> fileContent.append(s));
        return fileContent.toString();
    }

    /**
     * Datei einlesen
     *
     * @param file
     * @return
     */
    private static List<String> readFile(File file) {
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
        return lines;
    }

    public List<MatcherConfiguration> createMatcherConfiguration(String responseFileDir) {
        File file = new File(responseFileDir + File.separator + matchingConfigurationFilename);
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
            if (configItem == null) {
                throw new AppException("Invalid JSON : Empty matcher object");
            }
            if (configItem.getRequest() == null) {
                throw new AppException("Invalid JSON : Empty request object");
            }
            if (configItem.getResponse() == null) {
                throw new AppException("Invalid JSON : Empty response object");
            }

            if (configItem.getRequest().getUrlRegEx() != null) {
                configItem.getRequest().setUrlRegExMatchPattern(Pattern.compile(configItem.getRequest().getUrlRegEx()));
            }
            if (configItem.getRequest().getBodyRegEx() != null) {
                configItem.getRequest().setBodyRegExMatchPattern(Pattern.compile(configItem.getRequest().getBodyRegEx()));
            }

            if (configItem.getResponse().getFilename() == null) {
                throw new AppException("Invalid JSON : Empty response.filename");
            }
            String absFilename = responseFileDir + File.separatorChar + configItem.getResponse().getFilename();
            configItem.getResponse().setFile(new File(absFilename));
        }
        new ConfigurationValidator(matcherConfiguration).validate();
        return matcherConfiguration;
    }

}
