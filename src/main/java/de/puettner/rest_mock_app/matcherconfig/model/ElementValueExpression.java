package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class ElementValueExpression {

    public static final String REGEX_PREFIX = "regex::";
    public static final String FILE_PREFIX = "file::";

    protected final String expression;
    protected String value;
    protected boolean isFileExpression = false;
    protected boolean isRegularExpression = false;
    protected boolean isPlainExpression = false;
    private File file;
    private Pattern pattern;

    public ElementValueExpression(String expression) {
        this.expression = expression;
        if (isNotBlank(expression)) {
            if (expression.startsWith(FILE_PREFIX)) {
                this.isFileExpression = true;
            } else if (expression.startsWith(REGEX_PREFIX)) {
                this.isRegularExpression = true;
            } else {
                this.value = expression;
                this.isPlainExpression = true;
            }
        }
    }

    public static ElementValueExpression buildByRegEx(String regex) {
        return new ElementValueExpression(REGEX_PREFIX + regex).init();
    }

    public static ElementValueExpression buildByFile(String filename, String responseFileBaseDir) {
        return new ElementValueExpression(FILE_PREFIX + filename).init(responseFileBaseDir);
    }

    public static ElementValueExpression buildByString(String string) { return new ElementValueExpression(string).init();}

    private ElementValueExpression init() {
        this.init(null);
        return this;
    }

    public ElementValueExpression init(String responseFileBaseDir) {
        if (isFileExpression) {
            if (isBlank(responseFileBaseDir)) {
                throw new AppException("Invalid directory for responseFileBaseDir");
            }
            this.value = expression.trim().substring(6);
            String filename = responseFileBaseDir;
            if (value.startsWith("/") || value.startsWith("\\")) {
                value =  value.substring(1);
            }
            filename += File.separatorChar + value;
            file = new File(filename);
            if (!file.exists()) {
                throw new AppException("File not found " + filename);
            }
        } else if (isRegularExpression) {
            this.value = expression.substring(7);
            pattern = Pattern.compile(value);
        }
        return this;
    }

    public boolean isPlainExpression() {
        return isPlainExpression;
    }

    public boolean isFileExpression() {
        return isFileExpression;
    }

    public boolean isRegularExpression() {
        return isRegularExpression;
    }

    public void validate() {
        if (isFileExpression && !file.isFile()) {
            throw new AppException("File not found " + file.toString());
        }
    }

    public File getFile() {
        if (!isFileExpression) {
            throw new AppException("A file is not available for " + expression);
        }
        return file;
    }
    public Pattern getRegEx() {
        if (!isRegularExpression) {
            throw new AppException("A regular expression is not available for " + expression);
        }
        return pattern;
    }

    public String getContent() {
        if (isFileExpression) {
            try {
                return FileUtils.readFileToString(file, Charset.forName("UTF-8"));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return "";
            }
        }
        return value;
    }

    public boolean matches(String requestValue) {
        boolean result = false;
        String compareMode = "equal to";
        if (isPlainExpression) {
            result = this.value.equals(requestValue);
        } else if (isRegularExpression) {
            compareMode = "matches with";
            result = this.getRegEx().matcher(requestValue).matches();
        } else if (isFileExpression) {
            throw new AppException(MessageFormat.format("Can't compare {0} with a file {1}", requestValue, file.getName()));
        }
        log.debug(MessageFormat.format("''{0}'' {1} ''{2}'' ? {3}", this.value, compareMode, requestValue, result));
        return result;
    }
}
