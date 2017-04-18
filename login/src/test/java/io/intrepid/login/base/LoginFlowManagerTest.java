package io.intrepid.login.base;

import org.junit.Test;

import io.intrepid.login.testutils.BaseFlowManagerTest;
import io.intrepid.login.testutils.MockResponseObject;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginFlowManagerTest extends BaseFlowManagerTest<LoginFlowManager<MockResponseObject>> {

    private MockResponseObject mockResponse = new MockResponseObject();
    private final PublishSubject<Object> mockLoginButtonObservable = PublishSubject.create();

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
        TestLoginFlowManager.Builder<MockResponseObject> builder = new TestLoginFlowManager.Builder<>();
        builder.setLoginButtonObservable(mockLoginButtonObservable)
                .setLoginFlowCallbacks(mockLoginFlowCallbacks)
                .setLoginView(mockLoginView)
                .setLoginService(mockLoginService);
        new TestLoginFlowManager(builder);

        verify(mockLoginService, never()).getLoginObservable();
        verify(mockLoginFlowCallbacks, never()).onLoginSuccess(any());
        verify(mockLoginFlowCallbacks, never()).onLoginError(any());
    }

    private static class TestLoginFlowManager extends LoginFlowManager<MockResponseObject> {
        TestLoginFlowManager(Builder<MockResponseObject> builder) {
            super(builder);
        }

        static class Builder<T> extends LoginFlowManager.Builder<T, Builder<T>> {

            @Override
            public Builder<T> getBuilder() {
                return this;
            }
        }
    }
}
