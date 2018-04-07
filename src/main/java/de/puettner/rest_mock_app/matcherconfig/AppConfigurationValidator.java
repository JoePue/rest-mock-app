package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.List;

public class AppConfigurationValidator {

    private List<AppConfiguration> appConfiguration;

    public AppConfigurationValidator(List<AppConfiguration> appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    static void validateDir(String responseFileDir) {
        File file = new File(responseFileDir);
        if (!file.isDirectory()) {
            throw new AppException("Directory is invalid : ''{0}''", file);
        }
    }

    private static RequestMethod validateHttpMethod(String method) {
        try {
            return RequestMethod.valueOf(method.trim().toUpperCase());
        } catch (Exception e) {
            throw new AppException("Invalid HTTP method : ''{0}''" + method);
        }
    }

    public static File validateFile(File file) {
        if (!file.isFile()) {
            throw new AppException("File not found : ''{0}''" + file);
        }
        return file;
    }

    private static void validateHttpStatusCode(Integer value) {
        if (value < 1 || value > 599) {
            throw new AppException("Invalid int value :  : ''{0}'' " + value);
        }
    }

    void validate() {
        if (appConfiguration == null) {
            throw new AppException("Invalid JSON : Empty matcher object");
        }
        for (AppConfiguration item : appConfiguration) {
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
