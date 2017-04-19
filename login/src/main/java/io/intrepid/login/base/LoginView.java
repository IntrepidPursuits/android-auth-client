package io.intrepid.login.base;

import io.reactivex.Observable;

/**
 * Base Login View interface that all login views inherit from.
 **/
public interface LoginView {
    Observable<Object> getLoginButtonObservable();
}
