package io.intrepid.android_auth_client.service;

import java.util.concurrent.TimeUnit;

import io.intrepid.auth.base.LoginService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExampleLoginService extends LoginService<String> {
    @Override
    public Observable<String> getLoginObservable() {
        return Observable.defer(() -> Observable.just(username))
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
