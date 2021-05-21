package com.castify.tv.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.castify.tv.R;
import com.castify.tv.models.VideoCard;
import com.castify.tv.utils.FontStyles;

import java.util.ArrayList;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.MyView> {
    private ArrayList<VideoCard> videoCards;
    Context aContext;

    public RowAdapter(ArrayList<VideoCard> videoCards, Context aContext) {
        this.videoCards = videoCards;
        this.aContext = aContext;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.video_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        VideoCard videoCard = videoCards.get(position);
        holder.videoDescription.setText(videoCard.getDescription());
        Glide.with(aContext).asBitmap().load(videoCard.getThumbnail()).into(holder.videoThumbnail);

    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return videoCards.size();
    }


    public class MyView extends RecyclerView.ViewHolder {

        // Text View
        TextView videoDescription;
        ImageView videoThumbnail;

        public MyView(View view) {
            super(view);
            videoDescription = view.findViewById(R.id.videoDescription);
            videoThumbnail = view.findViewById(R.id.videoThumbnail);
            FontStyles.setFontArimoBold(aContext, videoDescription, false);

        }
    }
}
