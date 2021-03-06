package io.intrepid.login.validation;

public abstract class ValidationRule<V extends ValidationCallbacks> {

    protected V validationCallbacks;

    protected ValidationRule() {

    }

    protected ValidationRule(V validationCallbacks) {
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
