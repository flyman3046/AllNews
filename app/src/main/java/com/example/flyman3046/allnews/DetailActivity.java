package com.example.flyman3046.allnews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.flyman3046.allnews.Model.DataConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 *
 */
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private String mUrl;
    private String mSourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

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
        new ContentCrawl().execute(mUrl);
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

    // Title AsyncTask
    private class ContentCrawl extends AsyncTask<String, Void, Void> {
        Elements element;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DetailActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(params[0]).get();
                // Get the html document title

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView txtTitle = (TextView) findViewById(R.id.doc_content);
            txtTitle.setText(Html.fromHtml(element.toString()));

            mProgressDialog.dismiss();
        }
    }
}
