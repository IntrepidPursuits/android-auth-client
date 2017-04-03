package io.intrepid.auth.validation;

public abstract class ValidationRule {

    protected ValidationCallbacks validationCallbacks;

    protected ValidationRule() {

    }

    protected ValidationRule(ValidationCallbacks validationCallbacks) {
        this.validationCallbacks = validationCallbacks;
    }

    public final boolean validate(String input) {
        boolean valid = internalValidate(input);

        if (validationCallbacks != null) {
            if (valid) {
                validationCallbacks.onValidationSuccess(input);
            } else {
                validationCallbacks.onValidationFailure(input);
            }
        }

        return valid;
    }

    protected abstract boolean internalValidate(String input);
}
