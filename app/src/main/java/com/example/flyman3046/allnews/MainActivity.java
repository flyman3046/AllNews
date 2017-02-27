package com.example.flyman3046.allnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate in MainActivity");
        setContentView(R.layout.activity_main);
        loadSearchItem();
    }

    private void loadSearchItem() {
        Log.wtf(TAG, "Start to get json");
        ApiClient.ApiStores apiStores = ApiClient.retrofit().create(ApiClient.ApiStores.class);

        Observable<SourceResponse> observable = apiStores.getSources();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SourceResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.wtf(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e.toString());
                    }

                    @Override
                    public void onNext(SourceResponse result) {
                        Log.wtf(TAG, "search result size is: " + result.getStatus());
                    }
                });
    }
}
