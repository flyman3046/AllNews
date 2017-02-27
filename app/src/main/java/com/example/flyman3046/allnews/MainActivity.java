package com.example.flyman3046.allnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Source> mSourceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate in MainActivity");
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MainNewsAdapter(this, mSourceList);
        mRecyclerView.setAdapter(mAdapter);

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
                        mSourceList.clear();
                        mSourceList.addAll(result.getSources());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
