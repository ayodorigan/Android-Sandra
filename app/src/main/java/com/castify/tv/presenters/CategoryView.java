package com.castify.tv.presenters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.BaseCardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.castify.tv.R;
import com.castify.tv.utils.GlideApp;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

public class CategoryView extends BaseCardView {
    private ImageView categoryThumbnail;
    private TextView categoryTitle;
    String categoryType;

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildCardView();
    }

    public CategoryView(Context context, String categoryType) {
        super(context);
        this.categoryType = categoryType;
        buildCardView();
    }

    protected void buildCardView() {
        // Make sure this view is clickable and focusable
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (categoryType.equals("story")) {
            inflater.inflate(R.layout.category_story, this);

        } else if (categoryType.equals("paralaxBox")) {
            inflater.inflate(R.layout.category_basic, this);

        } 

        categoryThumbnail = findViewById(R.id.categoryThumbnail);
        categoryTitle = findViewById(R.id.categoryTitle);
    }

    /**
     * Sets the image drawable.
     */
    @SuppressLint("CheckResult")
    public void setMainImage(String url) {
        if(url == null || url.isEmpty()) {
            return;
        }
        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap();
        requestBuilder.load(GlobalVars.graphics.getDefaultThumbnail());
        GlideApp.with(categoryThumbnail.getContext())
                .asBitmap()
                .load(url)
                .thumbnail(requestBuilder)
                .into(categoryThumbnail);
    }



    public void setTitleText(CharSequence text) {
        if (categoryTitle == null) {
            return;
        }
        categoryTitle.setText(text);
    }


    public void setCategoryThumbnail(String categoryThumbnail) {
        GlobalFuncs.loadImage(categoryThumbnail, getContext(), this.categoryThumbnail);
    }



    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle .setText(categoryTitle);
    }
}