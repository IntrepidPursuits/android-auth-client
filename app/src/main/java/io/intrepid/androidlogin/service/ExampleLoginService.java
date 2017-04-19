package io.intrepid.androidlogin.service;

import io.intrepid.androidlogin.model.LoginCredentials;
import io.intrepid.androidlogin.model.User;
import io.intrepid.androidlogin.rest.RetrofitClient;
import io.intrepid.login.base.LoginService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExampleLoginService extends LoginService<User> {
    @Override
    public Observable<User> getLoginObservable() {
        return RetrofitClient.getApi().signIn(new LoginCredentials(username, password))
                .subscribeOn(Schedulers.io())
                .map(wrapper -> wrapper.payload)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
