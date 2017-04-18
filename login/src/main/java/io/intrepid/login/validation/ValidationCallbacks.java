package io.intrepid.login.validation;

public interface ValidationCallbacks {

    void onValidationSuccess(String input);

    void onValidationFailure(String input);
}
