package io.intrepid.login.basic;

import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.login.testutils.BaseFlowManagerTest;
import io.intrepid.login.testutils.MockResponseObject;
import io.intrepid.login.validation.NonEmptyValidationRule;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BasicLoginFlowManagerTest extends BaseFlowManagerTest<BasicLoginFlowManager> {

    @Mock
    BasicLoginView mockLoginView;
    @Mock
    EditText mockUsernameView;
    @Mock
    EditText mockPasswordView;

    private final PublishSubject<TextViewTextChangeEvent> mockUsernameTextFieldObservable = PublishSubject.create();
    private final PublishSubject<TextViewTextChangeEvent> mockPasswordTextFieldObservable = PublishSubject.create();
    private final PublishSubject<Object> mockLoginButtonObservable = PublishSubject.create();
    private final MockResponseObject mockResponse = new MockResponseObject();

    @Test
    public void setupTextWatching_noValidation() {
        loginFlowManager = new BasicLoginFlowManager.Builder<MockResponseObject>()
                .setLoginService(mockLoginService)
                .setLoginView(mockLoginView)
                .setLoginFlowCallbacks(mockLoginFlowCallbacks)
                .setUsernameObservable(mockUsernameTextFieldObservable, null)
                .setPasswordObservable(mockPasswordTextFieldObservable, null)
                .build();

        publishUsernameEvent("");
        publishPasswordEvent("");
        testScheduler.triggerActions();

        verify(mockLoginView, never()).disableLoginButton();
        verify(mockLoginView).enableLoginButton();
        assertThat(loginFlowManager.getUsername(), is(""));
        assertThat(loginFlowManager.getPassword(), is(""));
    }

    @Test
    public void setupTextWatching_withValidation() {
        loginFlowManager = new BasicLoginFlowManager.Builder<MockResponseObject>()
                .setLoginService(mockLoginService)
                .setLoginView(mockLoginView)
                .setLoginFlowCallbacks(mockLoginFlowCallbacks)
                .setUsernameObservable(mockUsernameTextFieldObservable, new NonEmptyValidationRule())
                .setPasswordObservable(mockPasswordTextFieldObservable, new NonEmptyValidationRule())
                .build();

        testScheduler.triggerActions();
        verify(mockLoginView, never()).enableLoginButton();

        publishUsernameEvent("");
        publishPasswordEvent("");
        testScheduler.triggerActions();

        verify(mockLoginView, never()).enableLoginButton();
        assertThat(loginFlowManager.getUsername(), is(""));
        assertThat(loginFlowManager.getPassword(), is(""));

        publishUsernameEvent("username");
        testScheduler.triggerActions();

        verify(mockLoginView, never()).enableLoginButton();
        assertThat(loginFlowManager.getUsername(), is("username"));
        assertThat(loginFlowManager.getPassword(), is(""));

        publishPasswordEvent("password");
        testScheduler.triggerActions();

        verify(mockLoginView).enableLoginButton();
        assertThat(loginFlowManager.getUsername(), is("username"));
        assertThat(loginFlowManager.getPassword(), is("password"));
    }

    @Test
    public void setupTextWatching_noTextObservables() {
        loginFlowManager = new BasicLoginFlowManager.Builder<MockResponseObject>()
                .setLoginService(mockLoginService)
                .setLoginView(mockLoginView)
                .setLoginFlowCallbacks(mockLoginFlowCallbacks)
                .build();

        testScheduler.triggerActions();

        verify(mockLoginView).enableLoginButton();
        assertNull(loginFlowManager.getUsername());
        assertNull(loginFlowManager.getPassword());
    }

    @Test
    public void loginButtonWatching() {
        loginObservableSetup();
        when(mockLoginService.getLoginObservable()).thenReturn(Observable.just(mockResponse));

        mockLoginButtonObservable.onNext(new Object());
        testScheduler.triggerActions();

        verify(mockLoginService).getLoginObservable();
        verify(mockLoginFlowCallbacks).onLoginSuccess(mockResponse);
        verify(mockLoginFlowCallbacks, never()).onLoginError(any());
    }

    @Test
    public void loginButtonWatching_loginFailure() {
        loginObservableSetup();
        Throwable error = new Throwable();
        when(mockLoginService.getLoginObservable()).thenReturn(Observable.error(error));

        mockLoginButtonObservable.onNext(new Object());
        testScheduler.triggerActions();

        verify(mockLoginService).getLoginObservable();
        verify(mockLoginFlowCallbacks, never()).onLoginSuccess(any());
        verify(mockLoginFlowCallbacks).onLoginError(error);
    }

    private void loginObservableSetup() {
        BasicLoginFlowManager.Builder<MockResponseObject> builder = new BasicLoginFlowManager.Builder<>();
        builder.setLoginButtonObservable(mockLoginButtonObservable)
                .setLoginFlowCallbacks(mockLoginFlowCallbacks)
                .setLoginView(mockLoginView)
                .setLoginService(mockLoginService)
                .build();

        verify(mockLoginService, never()).getLoginObservable();
        verify(mockLoginFlowCallbacks, never()).onLoginSuccess(any());
        verify(mockLoginFlowCallbacks, never()).onLoginError(any());
    }

    private void publishTextChangeEvent(PublishSubject<TextViewTextChangeEvent> mock, TextView textField, String text) {
        mock.onNext(TextViewTextChangeEvent.create(textField, text, 0, 0, 0));
    }

    private void publishUsernameEvent(String text) {
        publishTextChangeEvent(mockUsernameTextFieldObservable, mockUsernameView, text);
    }

    private void publishPasswordEvent(String text) {
        publishTextChangeEvent(mockPasswordTextFieldObservable, mockPasswordView, text);
    }
}
