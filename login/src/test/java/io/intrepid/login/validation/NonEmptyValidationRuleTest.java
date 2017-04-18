package io.intrepid.login.validation;

import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.login.testutils.BaseUnitTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class NonEmptyValidationRuleTest extends BaseUnitTest {

    @Mock
    ValidationCallbacks mockValidationCallbacks;

    private NonEmptyValidationRule rule;

    @Test
    public void validate() {
        rule  = new NonEmptyValidationRule();

        assertFalse(rule.validate(""));
        assertTrue(rule.validate("a"));
    }

    @Test
    public void validate_withCallbacks() {
        rule = new NonEmptyValidationRule(mockValidationCallbacks);

        assertFalse(rule.validate(""));
        verify(mockValidationCallbacks).onValidationFailure("");

        assertTrue(rule.validate("a"));
        verify(mockValidationCallbacks).onValidationSuccess("a");
    }
}
