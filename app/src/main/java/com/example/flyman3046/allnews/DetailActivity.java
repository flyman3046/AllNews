package com.example.flyman3046.allnews;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flyman3046.allnews.Model.DataConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 */
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private String mUrl;
    private String mSourceName;
    private TextView mTxtTitle;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        mTxtTitle = (TextView) findViewById(R.id.doc_content);
        mImageView = (ImageView) findViewById(R.id.article_image);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.article_detail_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Need to prevent memory leak with weak reference
        mUrl = getIntent().getStringExtra(DataConstants.ARTICLE_LINK_MESSAGE);
        mSourceName = getIntent().getStringExtra(DataConstants.ARTICLE_SOURCE_NAME);
        Log.wtf(TAG, mUrl);

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportPostponeEnterTransition();
            Picasso.with(this)
                    .load(getIntent().getStringExtra(DataConstants.ARTICLE_URL_IMAGE))
                    .noFade()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }
        else {
            Picasso.with(this)
                    .load(getIntent().getStringExtra(DataConstants.ARTICLE_URL_IMAGE))
                    .noFade()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mImageView);
        }

        Observable.just(mUrl)
                .map(new Func1<String, Elements>() {
                    @Override
                    public Elements call(String url) {
                        Elements element = null;
                        try {
                            Document document = Jsoup.connect(url).get();
                            // Find correct selector based on source name
                            String searchKey = null;
                            String shortedUrl = mUrl.substring(0, Math.min(50, mUrl.length()));

                            for (String key: DataConstants.NEWS_SOURCE_TO_SHORT_URL_MAP.keySet()) {
                                if (shortedUrl.contains(DataConstants.NEWS_SOURCE_TO_SHORT_URL_MAP.get(key))) {
                                    searchKey = key;
                                }
                            }

                            if (searchKey != null) {
                                element = document
                                        .select(DataConstants.NEWS_URL_TO_SELECTOR_MAP.get(mSourceName));
                            }
                        }
                        catch (Exception e) {
                            return null;
                        }
                        return element;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Elements>() {
                    @Override
                    public void onCompleted() {
                        Log.wtf(TAG, "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Elements result) {
                        Log.wtf(TAG, "onNext");
                        if (result != null) {
                            mTxtTitle.setText(Html.fromHtml(result.toString()));
                        }
                        else {
                            Log.wtf(TAG, "return a null object");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
