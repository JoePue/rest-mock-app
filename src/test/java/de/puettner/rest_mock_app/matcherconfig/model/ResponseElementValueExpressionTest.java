package de.puettner.rest_mock_app.matcherconfig.model;

import de.puettner.rest_mock_app.exception.AppException;
import org.junit.Test;

import static de.puettner.rest_mock_app.matcherconfig.model.ElementValueExpressionTest.RESPONSE_FILE_BASE_DIR;
import static org.junit.Assert.*;

public class ResponseElementValueExpressionTest {

    @Test
    public void constructWithRegEx(){
        ElementValueExpression actual = new ResponseElementValueExpression("regex::.*");
        actual.init(RESPONSE_FILE_BASE_DIR);
        assertFalse(actual.isFileExpression());
        assertTrue(actual.isRegularExpression());
        assertFalse(actual.isPlainExpression());
        assertNotNull(actual.getRegEx());
        try {
            actual.getContent();
            assertFalse("Should not reached.", true);
        }catch (AppException e){}
    }
}