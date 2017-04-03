package io.intrepid.auth.validation;

public interface ValidationCallbacks {

    void onValidationSuccess(String input);

    void onValidationFailure(String input);
}
