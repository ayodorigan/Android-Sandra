package com.castify.tv.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.castify.tv.R;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.VideoCard;

import java.util.ArrayList;
import java.util.Objects;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyView>  implements OnItemViewSelectedListener {
    private final Context aContext;

    public VideosAdapter(Context aContext) {
        this.aContext = aContext;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView = LayoutInflater.from(aContext).inflate(R.layout.rows, parent, false);

        Log.e("Step", "4");


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
        if (position >= GlobalVars.videos.size()) {
            return;
        }

        Log.e("position", position+ "");


        Object key = Objects.requireNonNull(GlobalVars.videos.keySet().toArray())[position];
        ArrayList<VideoCard> videoCards = GlobalVars.videos.get(key);

        Log.e("key", key .toString()+ "");
        Log.e("videoCards", videoCards.size()+ "");

        // Set the text of each item of
        // Recycler view with the list items
        holder.rowTitle.setText(key.toString());
        holder.rowItemsCount.setText(String.valueOf(videoCards.size()));
        RecyclerView rowItemsRecyclerView  = holder.rowItems;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(aContext, LinearLayoutManager.HORIZONTAL, false);
        RowAdapter rowAdapter = new RowAdapter(videoCards, aContext);
        rowItemsRecyclerView.setAdapter(rowAdapter);
        rowItemsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return GlobalVars.videos.size();
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        Log.e("Selecetf", "true");
    }


    public class MyView extends RecyclerView.ViewHolder {

        // Text View
        TextView rowTitle, rowItemsCount;
        RecyclerView rowItems;

        public MyView(View view) {
            super(view);
            rowTitle = view.findViewById(R.id.rowTitle);
            rowItemsCount = view.findViewById(R.id.rowItemsCount);
            rowItems = view.findViewById(R.id.rowItems);
            FontStyles.setFontArimoBold(aContext, rowTitle, false);

        }
    }
}
