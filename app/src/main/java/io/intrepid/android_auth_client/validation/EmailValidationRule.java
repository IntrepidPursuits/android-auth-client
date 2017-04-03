package io.intrepid.android_auth_client.validation;

import java.util.regex.Pattern;

import io.intrepid.auth.validation.ValidationCallbacks;
import io.intrepid.auth.validation.ValidationRule;

public class EmailValidationRule extends ValidationRule {

    private static final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    public EmailValidationRule(ValidationCallbacks callbacks) {
        super(callbacks);
    }

    @Override
    protected boolean internalValidate(String input) {
        return EMAIL_REGEX.matcher(input).matches();
    }
}
