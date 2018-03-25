package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;

public class ResponseElementValueExpression extends ElementValueExpression {

    public ResponseElementValueExpression(String expression) {
        super(expression);
    }

    @Override
    public String getContent() {
        if (super.isRegularExpression) {
            throw new AppException("A regular expression as body is invalid.  expression: " + expression);
        }
        return super.getContent();
    }
}
