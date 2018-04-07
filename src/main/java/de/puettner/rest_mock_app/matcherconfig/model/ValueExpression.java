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

/**
 * Some configuration values can express special meanings if the have a predefined prefix <br>
 * <code>
 * {"key" : "value"}            => isPlainExpression = true <br>
 * {"key" : "file::value"}      => isFileExpression = true <br>
 * {"key" : "regex::value"}     => isRegularExpression = true <br>
 * </code>
 */
@Slf4j
public class ValueExpression {

    public static final String REGEX_PREFIX = "regex::";
    public static final String FILE_PREFIX = "file::";

    /** Possible schemas for an expression are '[value]' and '[operator]::[value]' */
    protected final String expression;
    protected String value;
    protected boolean isFileExpression = false;
    protected boolean isRegularExpression = false;
    private boolean isPlainExpression = false;
    private File file;
    private Pattern pattern;

    public ValueExpression(String expression) {
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

    public static ValueExpression buildByRegEx(String regex) {
        return new ValueExpression(REGEX_PREFIX + regex).init();
    }

    public static ValueExpression buildByFile(String filename, String responseFileBaseDir) {
        return new ValueExpression(FILE_PREFIX + filename).init(responseFileBaseDir);
    }

    public static ValueExpression buildByString(String string) { return new ValueExpression(string).init();}

    public ValueExpression init() {
        if (isFileExpression) {
            throw new AppException("This init method is not allowed to use for file expressions.");
        }
        this.init(null);
        return this;
    }

    public ValueExpression init(String responseFileBaseDir) {
        return init(responseFileBaseDir, false);
    }

    public ValueExpression init(String responseFileBaseDir, boolean headerNameToLowerCase) {
        if (isFileExpression) {
            if (isBlank(responseFileBaseDir)) {
                throw new AppException("Invalid directory for responseFileBaseDir");
            }
            this.value = expression.trim().substring(6);
            String filename = responseFileBaseDir;
            if (value.startsWith("/") || value.startsWith("\\")) {
                value = value.substring(1);
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
        if (headerNameToLowerCase) {
            this.value = this.value.toLowerCase();
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
            throw new AppException(MessageFormat.format("A file is not available for ''{0}''", expression));
        }
        return file;
    }

    public Pattern getRegEx() {
        if (!isRegularExpression) {
            throw new AppException("A regular expression is not available for " + expression);
        }
        return pattern;
    }

    public String getValue() {
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
