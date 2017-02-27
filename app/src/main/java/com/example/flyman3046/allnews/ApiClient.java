package com.example.flyman3046.allnews;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public class ApiClient {
    static Retrofit mRetrofit;
    private static final String BASEURL = "https://newsapi.org/v1/";
    private static final String APIKEY = "9204fb99bbb9437c95f5661a44bba683";

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public interface ApiStores {

        @GET("sources?language=en")
        Observable<SourceResponse> getSources();
    }
}
