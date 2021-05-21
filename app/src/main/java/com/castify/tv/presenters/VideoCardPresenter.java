package com.castify.tv.presenters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.leanback.widget.BaseCardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.castify.tv.R;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

public class VideoCardPresenter extends BaseCardView {
    private ImageView _imageView;
    private TextView duration;
    private CardView cardview;
    private TextView _titleView;
    private LinearLayout titleTextHolder;
    public VideoCardPresenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildCardView();
    }

    public VideoCardPresenter(Context context) {
        super(context);
        buildCardView();
    }

    protected void buildCardView() {
        // Make sure this view is clickable and focusable
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.my_card_view, this);

        cardview = findViewById(R.id.cardview);
        _imageView = findViewById(R.id.card_image);
        duration =  findViewById(R.id.duration);
        _titleView = findViewById(R.id.title_text);
        titleTextHolder = findViewById(R.id.titleTextHolder);
        FontStyles.setFontArimoBold(getContext(), duration, false);
        FontStyles.setFontArimoBold(getContext(), _titleView, false);

    }

    /**
     * Sets the image drawable.
     */
    @SuppressLint("CheckResult")
    public void setMainImage(String url) {
        if(url == null || url.isEmpty()) {
            return;
        }
//        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap();
//        requestBuilder.load(GlobalVars.graphics.getDefaultThumbnail());
//        GlideApp.with(getContext())
//                .asBitmap()
//                .load(url)
//                .thumbnail(requestBuilder)
//                .into(_imageView);
        GlobalFuncs.loadImage(url, _imageView.getContext(), _imageView);
    }


    public ImageView getMainImageView() {

        return _imageView;
    }
    /**
     * Sets the logo image drawable
     */
    public void setVideoDuration(String duration) {
        if (duration.equals("Live")) {
            this.duration.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        this.duration.setText(duration);
    }

    /**
     * Sets the title text.
     */
    public void setTitleText(CharSequence text) {
        if (_titleView == null) {
            return;
        }
        _titleView.setText(text);
    }

    public void radius(boolean addRadius) {
        if (addRadius) {
            cardview.setRadius(15);
            return;
        }
        cardview.setRadius(0);
    }

    public ImageView get_imageView() {
        return _imageView;
    }

    public void set_imageView(ImageView _imageView) {
        this._imageView = _imageView;
    }

    public TextView getDuration() {
        return duration;
    }

    public void setDuration(TextView duration) {
        this.duration = duration;
    }

    public TextView get_titleView() {
        return _titleView;
    }

    public void set_titleView(TextView _titleView) {
        this._titleView = _titleView;
    }
}