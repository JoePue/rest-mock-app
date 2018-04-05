package de.puettner.rest_mock_app.matcherconfig;

import java.io.File;

import org.springframework.web.bind.annotation.RequestMethod;

import de.puettner.rest_mock_app.exception.AppException;

public class Validators {
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
