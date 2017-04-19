package io.intrepid.androidlogin.example;

import io.intrepid.androidlogin.model.User;
import io.intrepid.androidlogin.service.ExampleLoginService;
import io.intrepid.androidlogin.validation.EmailValidationRule;
import io.intrepid.login.base.LoginFlowCallbacks;
import io.intrepid.login.basic.BasicLoginFlowManager;
import io.intrepid.login.validation.NonEmptyValidationRule;
import io.intrepid.login.validation.ValidationCallbacks;
import timber.log.Timber;


class ExampleLoginPresenter implements ExampleLoginContract.Presenter, LoginFlowCallbacks<User>, ValidationCallbacks {

    private ExampleLoginContract.View view;
    private final BasicLoginFlowManager<User, ExampleLoginContract.View> loginFlowManager;

    ExampleLoginPresenter(ExampleLoginContract.View view) {
        this.view = view;
        loginFlowManager = new BasicLoginFlowManager.Builder<User, ExampleLoginContract.View>()
                .setLoginView(view)
                .setUsernameObservable(view.getUsernameTextFieldObservable(), new EmailValidationRule(this))
                .setPasswordObservable(view.getPasswordTextFieldObservable(), new NonEmptyValidationRule(this))
                .setLoginFlowCallbacks(this)
                .setLoginService(new ExampleLoginService())
                .setLoginButtonObservable(view.getLoginButtonObservable())
                .build();
    }

    @Override
    public void onLoginSuccess(User response) {
        view.showToast(response.name);
    }

    @Override
    public void onLoginError(Throwable throwable) {
        Timber.v(throwable);
        view.showToast("something went wrong");
    }

    @Override
    public void onValidationSuccess(String input) {
        if (input.equals(loginFlowManager.getUsername())) {
            Timber.i("email ok");
        } else if (input.equals(loginFlowManager.getPassword())) {
            Timber.i("password ok");
        }
    }

    @Override
    public void onValidationFailure(String input) {
        Timber.i("invalid input: %s", input);
    }
}
