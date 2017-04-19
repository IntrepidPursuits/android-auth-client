package io.intrepid.login.validation;

public class NonEmptyValidationRule extends ValidationRule<ValidationCallbacks> {

    public NonEmptyValidationRule() {
    }

    public NonEmptyValidationRule(ValidationCallbacks validationCallbacks) {
        super(validationCallbacks);
    }

    @Override
    protected boolean internalValidate(String input) {
        return input.length() > 0;
    }
}
