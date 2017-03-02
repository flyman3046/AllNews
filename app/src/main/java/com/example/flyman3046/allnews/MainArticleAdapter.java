package com.example.flyman3046.allnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flyman3046.allnews.Model.Article;
import com.example.flyman3046.allnews.Model.DataConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.NewsCardViewHolders> {
    private List<Article> mStoreList;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainArticleAdapter(Context context, List<Article> myDataset) {
        mContext = context;
        mStoreList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsCardViewHolders onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new NewsCardViewHolders(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewsCardViewHolders holder, int position) {
        final Article obj = mStoreList.get(position);
        Picasso.with(mContext).load(obj.getUrlToImage()).into(holder.articleImage);
        holder.articleTitle.setText(obj.getTitle());
        holder.articlePublishedAt.setText(obj.getPublishedAt());
    }

    @Override
    public int getItemCount() {
        return mStoreList.size();
    }

    public class NewsCardViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView articleImage;
        public TextView articleTitle;
        public TextView articlePublishedAt;

        public NewsCardViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            articleImage = (ImageView) itemView.findViewById(R.id.article_image);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            articlePublishedAt = (TextView) itemView.findViewById(R.id.article_published_at);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DataConstants.ARTICLE_LINK_MESSAGE, mStoreList.get(getPosition()).getUrl());
            mContext.startActivity(intent);
        }
    }
}