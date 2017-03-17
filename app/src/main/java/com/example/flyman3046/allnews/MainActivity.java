package com.example.flyman3046.allnews;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.flyman3046.allnews.Model.ApiClient;
import com.example.flyman3046.allnews.Model.Article;
import com.example.flyman3046.allnews.Model.ArticleResponse;
import com.example.flyman3046.allnews.Model.DataConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Article> mArticlesList = new ArrayList<>();
    private List<String> mSourceList = new ArrayList<>();

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Your refresh code here
                    loadSearchItem();
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MainArticleAdapter(this, mArticlesList, mSourceList);
        mRecyclerView.setAdapter(mAdapter);

        loadSearchItem();
    }

    private void loadSearchItem() {
        Log.wtf(TAG, "Start to get json");
        final ApiClient.ApiStores apiStores = ApiClient.retrofit().create(ApiClient.ApiStores.class);
        Set<String> source = DataConstants.NEWS_SOURCE_TO_SHORT_URL_MAP.keySet();

        Observable<ArticleResponse> observable = Observable.from(source).concatMap(new Func1<String, Observable<ArticleResponse>>() {
            @Override public Observable<ArticleResponse> call(String str) {
                return apiStores.getArticles(str, ApiClient.APIKEY);
            }
        });

        mSourceList.clear();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticleResponse>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.notifyDataSetChanged();
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        Log.wtf(TAG, "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e.toString());
                    }

                    @Override
                    public void onNext(ArticleResponse result) {
                        Log.wtf(TAG, "search result size is: " + result.getStatus());
                        mArticlesList.addAll(result.getArticles());
                        for (int i = 0; i < result.getArticles().size(); i++) {
                            mSourceList.add(result.getSource());
                        }
                    }
                });
    }
}
