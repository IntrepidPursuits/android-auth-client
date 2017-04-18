package io.intrepid.androidlogin.example;

import io.intrepid.androidlogin.service.ExampleLoginService;
import io.intrepid.androidlogin.validation.EmailValidationRule;
import io.intrepid.login.base.LoginFlowCallbacks;
import io.intrepid.login.basic.BasicLoginFlowManager;
import io.intrepid.login.validation.NonEmptyValidationRule;
import io.intrepid.login.validation.ValidationCallbacks;


class ExampleLoginPresenter implements ExampleLoginContract.Presenter, LoginFlowCallbacks<String>, ValidationCallbacks {

    private ExampleLoginContract.View view;
    private BasicLoginFlowManager<String> loginFlowManager;

    ExampleLoginPresenter(ExampleLoginContract.View view) {
        this.view = view;
        loginFlowManager = new BasicLoginFlowManager.Builder<String>()
                .setLoginView(view)
                .setUsernameObservable(view.getUsernameTextFieldObservable(), new EmailValidationRule(this))
                .setPasswordObservable(view.getPasswordTextFieldObservable(), new NonEmptyValidationRule(this))
                .setLoginFlowCallbacks(this)
                .setLoginService(new ExampleLoginService())
                .setLoginButtonObservable(view.getLoginButtonObservable())
                .build();
    }

    @Override
    public void onLoginSuccess(String response) {
        if (response != null) {
            view.showToast(response);
        } else {
            view.showToast("click");
        }
    }

    @Override
    public void onLoginError(Throwable throwable) {
        view.showToast("something went wrong");
    }

    @Override
    public void onValidationSuccess(String input) {
        if (input.equals(loginFlowManager.getUsername())) {
            view.showToast("email ok");
        } else if (input.equals(loginFlowManager.getPassword())) {
            view.showToast("password ok");
        }
    }

    @Override
    public void onValidationFailure(String input) {

    }
}
