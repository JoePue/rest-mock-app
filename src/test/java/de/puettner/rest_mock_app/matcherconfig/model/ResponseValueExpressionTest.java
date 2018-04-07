package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseValueExpressionTest {

    @Test
    public void constructWithRegEx() {
        ValueExpression actual = new ResponseValueExpression("regex::.*");
        actual.init();
        assertFalse(actual.isFileExpression());
        assertTrue(actual.isRegularExpression());
        assertFalse(actual.isPlainExpression());
        assertNotNull(actual.getRegEx());
        try {
            actual.getValue();
            assertFalse("Should not reached.", true);
        } catch (AppException e) {
        }
    }
}