package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.RequestConfiguration;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.List;

public class AppConfigurationValidator {

    private final String responseFileBaseDir;

    public AppConfigurationValidator(String responseFileBaseDir) {
        this.responseFileBaseDir = responseFileBaseDir;
    }

    public void validate(List<AppConfiguration> appConfigurations) {
        if (appConfigurations == null) {
            throw new AppException("Invalid JSON : Empty matcher object");
        }
        for (AppConfiguration item : appConfigurations) {
            if (item.getRequest() == null) {
                throw new AppException("Invalid JSON : Empty request object");
            }
            if (item.getResponse() == null) {
                throw new AppException("Invalid JSON : Empty responseConfig object");
            }
            validateRequestConfig(item.getRequest());
            validateResponseConfig(item.getResponse());
            //            validateHttpStatusCode(item.getResponse().getStatusCode());
            //            item.getResponse().getBody().validate();
        }
    }

    public void validateRequestConfig(RequestConfiguration requestConfig) {
        if (requestConfig == null) {
            throw new AppException("Invalid JSON : Empty request object");
        }
        if (requestConfig.getHeader() != null) {
            if (requestConfig.getHeader().getName() == null || requestConfig.getHeader().getValue() == null) {
                throw new AppException("Invalid JSON : Header name and header value must be set.");
            }
        }
        if (requestConfig.getMethod() != null && requestConfig.getMethod().isPlainExpression()) {
            validateHttpMethod(requestConfig.getMethod().getValue());
        }
    }

    public void validateResponseConfig(ResponseConfiguration responseConfig) {
        if (responseConfig == null) {
            throw new AppException("Invalid JSON : Empty responseConfig object");
        }
        if (responseConfig.getBody() == null) {
            throw new AppException("Invalid JSON : Empty responseConfig.body");
        }
        validateHttpStatusCode(responseConfig.getStatusCode());
    }

    static void validateDir(String responseFileDir) {
        File file = new File(responseFileDir);
        if (!file.isDirectory()) {
            throw new AppException("Directory is invalid param=" + file);
        }
    }

    static RequestMethod validateHttpMethod(String method) {
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

    static void validateHttpStatusCode(Integer value) {
        if (value < 1 || value > 599) {
            throw new AppException("Invalid int value. value = " + value);
        }
    }
}
