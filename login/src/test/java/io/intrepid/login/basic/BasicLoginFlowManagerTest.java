package io.intrepid.login.basic;

import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.login.testutils.BaseFlowManagerTest;
import io.intrepid.login.testutils.MockResponseObject;
import io.intrepid.login.validation.NonEmptyValidationRule;
import io.reactivex.subjects.PublishSubject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BasicLoginFlowManagerTest extends BaseFlowManagerTest<BasicLoginFlowManager<MockResponseObject>> {

    @Mock
    EditText mockUsernameView;
    @Mock
    EditText mockPasswordView;

    private PublishSubject<TextViewTextChangeEvent> mockUsernameTextFieldObservable = PublishSubject.create();
    private PublishSubject<TextViewTextChangeEvent> mockPasswordTextFieldObservable = PublishSubject.create();

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
