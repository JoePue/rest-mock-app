package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ValueExpressionTest {

    static final String RESPONSE_FILE_BASE_DIR = "./src/test/resources".replace("/", File.separator);

    @Test
    public void constructWithNullValue() {
        assertInvalidElementValueExpression(null);
    }

    @Test
    public void constructWithEmptyValue() {
        assertInvalidElementValueExpression("");
    }

    private void assertInvalidElementValueExpression(String value) {
        ValueExpression actual = new ValueExpression(value);
        actual.init(RESPONSE_FILE_BASE_DIR);
        assertFalse(actual.isFileExpression());
        assertFalse(actual.isRegularExpression());
        assertFalse(actual.isPlainExpression());
        try {
            actual.getFile();
            assertFalse("Should not reached.", true);
        } catch (AppException e) {
        }
        try {
            actual.getRegEx();
            assertFalse("Should not reached.", true);
        } catch (AppException e) {
        }
    }

    @Test(expected = AppException.class)
    public void initWithNullString() {
        ValueExpression.buildByFile("plain", null);
    }

    @Test(expected = AppException.class)
    public void initWithEmptyString() {
        ValueExpression.buildByFile("plain", "");
    }

    @Test
    public void constructWithRegEx() {
        ValueExpression actual = new ValueExpression("regex::.*");
        actual.init(RESPONSE_FILE_BASE_DIR);
        assertFalse(actual.isFileExpression());
        assertTrue(actual.isRegularExpression());
        assertFalse(actual.isPlainExpression());
        assertNotNull(actual.getRegEx());
        actual.getValue();
    }

    @Test
    public void constructWithPlainString() {
        ValueExpression actual = new ValueExpression("plain");
        actual.init(RESPONSE_FILE_BASE_DIR);
        assertFalse(actual.isFileExpression());
        assertFalse(actual.isRegularExpression());
        assertTrue(actual.isPlainExpression());
        assertEquals("plain", actual.getValue());
        try {
            actual.getRegEx();
            assertFalse("Should not reached.", true);
        } catch (AppException e) {
        }
    }

    @Test
    public void constructWithFile() {
        String[] testFilenames = {"file::testFile.txt", "file::/testFile.txt"};
        for (String testFilename : testFilenames) {
            ValueExpression actual = new ValueExpression(testFilename);
            actual.init(RESPONSE_FILE_BASE_DIR);
            assertNotNull(actual.getFile());
            assertTrue(actual.isFileExpression());
            assertFalse(actual.isRegularExpression());
            assertFalse(actual.isPlainExpression());
            assertEquals("Content of testFile.txt", actual.getValue());
            try {
                actual.getRegEx();
                assertFalse("Should not reached.", true);
            } catch (AppException e) {
            }
        }
    }
}