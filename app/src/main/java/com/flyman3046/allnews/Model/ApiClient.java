package com.flyman3046.allnews.Model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class ApiClient {
    static Retrofit mRetrofit;
    private static final String BASEURL = "https://newsapi.org/v1/";
    public static final String APIKEY = "9204fb99bbb9437c95f5661a44bba683";

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

        @GET("articles?")
        Observable<ArticleResponse> getArticles(
                @Query("source") String source,
                @Query("apiKey") String apiKey);

    }
}
