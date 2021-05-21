package com.castify.tv.presenters;

import android.os.Bundle;

import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;

public class CustomVerticalGridPresenter extends VerticalGridPresenter {

    VerticalGridView gridView;

    public CustomVerticalGridPresenter(int zoom, boolean val){
        super(zoom, val);
    }

    @Override
    protected void initializeGridViewHolder(ViewHolder vh) {
        super.initializeGridViewHolder(vh);
        gridView = vh.getGridView();
        int top = 0;//this is the new value for top padding
        int bottom = gridView.getPaddingBottom();
        int right = gridView.getPaddingRight();
        int left = gridView.getPaddingLeft();
        gridView.setPadding(left,top,right,bottom);
    }
}