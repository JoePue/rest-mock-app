package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;
import org.junit.Test;

import static de.puettner.rest_mock_app.TestConstants.TESTFILE_TXT;
import static de.puettner.rest_mock_app.TestConstants.TEST_RESOURCES_DIR;
import static de.puettner.rest_mock_app.TestConstants.TEST_RESPONSES_DIR;
import static org.junit.Assert.*;

public class ValueExpressionTest {

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
        actual.init(TEST_RESOURCES_DIR);
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
        actual.init(TEST_RESOURCES_DIR);
        assertFalse(actual.isFileExpression());
        assertTrue(actual.isRegularExpression());
        assertFalse(actual.isPlainExpression());
        assertNotNull(actual.getRegEx());
        actual.getValue();
    }

    @Test
    public void constructWithPlainString() {
        ValueExpression actual = new ValueExpression("plain");
        actual.init(TEST_RESOURCES_DIR);
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
        String[] testFilenames = {"file::" + TESTFILE_TXT, "file::/" + TESTFILE_TXT};
        for (String testFilename : testFilenames) {
            ValueExpression actual = new ValueExpression(testFilename);
            actual.init(TEST_RESPONSES_DIR);
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