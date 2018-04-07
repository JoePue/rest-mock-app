package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;

public class ResponseValueExpression extends ValueExpression {

    public ResponseValueExpression(String expression) {
        super(expression);
    }

    @Override
    public String getValue() {
        if (super.isRegularExpression) {
            throw new AppException("A regular expression as body is invalid.  expression: " + expression);
        }
        return super.getValue();
    }
}
