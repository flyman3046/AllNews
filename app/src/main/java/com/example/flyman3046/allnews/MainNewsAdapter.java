package com.example.flyman3046.allnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainNewsAdapter extends RecyclerView.Adapter<MainNewsAdapter.NewsCardViewHolders> {
    private List<Source> mStoreList;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainNewsAdapter(Context context, List<Source> myDataset) {
        mContext = context;
        mStoreList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsCardViewHolders onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new NewsCardViewHolders(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewsCardViewHolders holder, int position) {
        final Source obj = mStoreList.get(position);
        Picasso.with(mContext).load(obj.getUrlsToLogos().getSmall()).into(holder.newsImage);
        holder.newsTitle.setText(obj.getName());
    }

    @Override
    public int getItemCount() {
        return mStoreList.size();
    }

    public class NewsCardViewHolders extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsTitle;
        public View card;

        public NewsCardViewHolders(View itemView) {
            super(itemView);
            card = itemView;

            newsImage = (ImageView) itemView.findViewById(R.id.item_photo);
            newsTitle = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}