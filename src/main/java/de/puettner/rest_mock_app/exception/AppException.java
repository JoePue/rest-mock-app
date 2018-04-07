package de.puettner.rest_mock_app.exception;

import org.springframework.validation.Errors;

import java.text.MessageFormat;

@SuppressWarnings("serial")
public class AppException extends RuntimeException {
    private Errors errors;

    public AppException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public AppException(String message, Exception e) {
        super(message, e);
    }

    public AppException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

}
