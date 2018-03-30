package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.MatcherConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.List;

public class ConfigurationValidator {

    private List<MatcherConfiguration> matcherConfiguration;

    public ConfigurationValidator(List<MatcherConfiguration> matcherConfiguration) {
        this.matcherConfiguration = matcherConfiguration;
    }

    static void validateDir(String responseFileDir) {
        File file = new File(responseFileDir);
        if (!file.isDirectory()) {
            throw new AppException("Directory is invalid param=" + file);
        }
    }

    private static RequestMethod validateHttpMethod(String method) {
        try {
            return RequestMethod.valueOf(method.trim().toUpperCase());
        } catch (Exception e) {
            throw new AppException("Invalid HTTP method. method = " + method);
        }
    }

    public static File validateFile(File file) {
        if (!file.isFile()) {
            throw new AppException("File not found. file = " + file);
        }
        return file;
    }

    private static void validateHttpStatusCode(Integer value) {
        if (value < 1 || value > 599) {
            throw new AppException("Invalid int value. value = " + value);
        }
    }

    void validate() {
        if (matcherConfiguration == null) {
            throw new AppException("Invalid JSON : Empty matcher object");
        }
        for (MatcherConfiguration item : matcherConfiguration) {
            if (item.getRequest() == null) {
                throw new AppException("Invalid JSON : Empty request object");
            }
            if (item.getResponse() == null) {
                throw new AppException("Invalid JSON : Empty response object");
            }
            if (item.getRequest().getMethod() != null && item.getRequest().getMethod().isPlainExpression()) {
                validateHttpMethod(item.getRequest().getMethod().getValue());
            }
            validateHttpStatusCode(item.getResponse().getStatusCode());
            item.getResponse().getBody().validate();
        }
    }
}
