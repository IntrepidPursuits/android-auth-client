package io.intrepid.androidlogin.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

public class RetrofitClient {
    private static final String HEADER_ACCEPT_VALUE = "application/vnd.sonde.com+json; version=1";
    private static final String BASE_URL = "https://sonde-server-staging.herokuapp.com";
    private static final int CONNECTION_TIMEOUT = 30;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);

    private static RestApi instance;

    public static RestApi getApi() {
        if (instance == null) {
            instance = createRestApi();
        }
        return instance;
    }

    private RetrofitClient() {

    }

    private static RestApi createRestApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new HttpLoggingInterceptor(message -> Timber.v(message)).setLevel(
                    HttpLoggingInterceptor.Level.BODY));
        builder.addInterceptor(chain -> {
            Request.Builder newRequestBuilder = chain.request().newBuilder();
            newRequestBuilder.addHeader("Accept", HEADER_ACCEPT_VALUE);

            return chain.proceed(newRequestBuilder.build());
        });

        OkHttpClient httpClient = builder
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestApi.class);
    }
}
