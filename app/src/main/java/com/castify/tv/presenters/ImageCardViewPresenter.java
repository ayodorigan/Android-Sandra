package com.castify.tv.presenters;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.leanback.widget.ImageCardView;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.VideoCard;

/**
 * A very basic {@link ImageCardView} {@link androidx.leanback.widget.Presenter}.You can
 * pass a custom style for the ImageCardView in the constructor. Use the default constructor to
 * create a Presenter with a default ImageCardView style.
 */
public class ImageCardViewPresenter extends AbstractCardPresenter<VideoCardPresenter> {
    private static final int MAX_LENGTH = 60;
    private PageGraphics pageGraphics;

    public ImageCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
    }
    public ImageCardViewPresenter(Context context, PageGraphics pageGraphics) {
        this(context, R.style.DefaultCardTheme);
        this.pageGraphics = pageGraphics;
    }


    @Override
    protected VideoCardPresenter onCreateView() {

        return null;
    }

    @Override
    public void onBindViewHolder(VideoCard videoCard, VideoCardPresenter cardView) {
        cardView.setAlpha(1.0f); //Remove dimming here.
        cardView.setTag(videoCard);
        String title = videoCard.getTitle();
        if (title.length() > MAX_LENGTH ) {
            title = title.substring(0, MAX_LENGTH);
            title = title + "...";
        }
        cardView.setTitleText(title);
        String duration = videoCard.isIs_live_streaming() ? "Live" : GlobalFuncs.getDurationString(Integer.parseInt(videoCard.getVideoDuration()));
        cardView.setVideoDuration(duration);
        cardView.setMainImage(videoCard.getThumbnail());
        ((TextView) cardView.findViewById(R.id.title_text)).setTextColor(Color.parseColor(GlobalVars.graphics.getTextColor()));

        if (pageGraphics != null) {
            ((TextView) cardView.findViewById(R.id.title_text)).setTextColor(Color.parseColor(pageGraphics.getText_color()));
        }

        cardView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ((LinearLayout)cardView.findViewById(R.id.titleTextHolder)).setBackgroundColor(Color.WHITE);
                ((TextView) cardView.findViewById(R.id.title_text)).setTextColor(Color.parseColor(GlobalVars.graphics.getMainColor()));
                cardView.radius(true);
            } else {
                ((LinearLayout)cardView.findViewById(R.id.titleTextHolder)).setBackgroundColor(Color.TRANSPARENT);
                ((TextView) cardView.findViewById(R.id.title_text)).setTextColor(Color.parseColor(GlobalVars.graphics.getTextColor()));
                cardView.radius(false);

            }
        });

    }



    public void updateTextColor(ImageCardView imageCardView, String color) {
        try {

            if (color != null && !color.isEmpty() ) {

                ((TextView) imageCardView.findViewById(R.id.title_text)).setTextColor(Color.parseColor(color)); // Title text
                ((TextView) imageCardView.findViewById(R.id.content_text)).setTextColor(Color.parseColor(color)); // Title text

            } else  {

                ((TextView) imageCardView.findViewById(R.id.title_text)).setTextColor(Color.BLACK); // Title text
                ((TextView) imageCardView.findViewById(R.id.content_text)).setTextColor(Color.BLACK); // Title text

            }


        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "ImageView Text Color", e);
        }
    }
}