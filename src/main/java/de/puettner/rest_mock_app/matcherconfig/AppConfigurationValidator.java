package de.puettner.rest_mock_app.matcherconfig;

import de.puettner.rest_mock_app.exception.AppException;
import de.puettner.rest_mock_app.matcherconfig.model.AppConfiguration;
import java.util.List;

import static de.puettner.rest_mock_app.matcherconfig.Validators.validateHttpMethod;
import static de.puettner.rest_mock_app.matcherconfig.Validators.validateHttpStatusCode;

public class AppConfigurationValidator {

    private List<AppConfiguration> appConfiguration;

    public AppConfigurationValidator(List<AppConfiguration> appConfiguration) {
        this.appConfiguration = appConfiguration;
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
