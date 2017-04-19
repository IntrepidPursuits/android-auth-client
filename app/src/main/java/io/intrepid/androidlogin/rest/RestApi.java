package io.intrepid.androidlogin.rest;

import io.intrepid.androidlogin.model.LoginCredentials;
import io.intrepid.androidlogin.model.ResponseWrapper;
import io.intrepid.androidlogin.model.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {

    @POST("/authentications")
    Observable<ResponseWrapper<User>> signIn(@Body LoginCredentials credentials);
}
