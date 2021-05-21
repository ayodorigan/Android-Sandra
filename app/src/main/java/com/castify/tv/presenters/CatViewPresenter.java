package com.castify.tv.presenters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

import androidx.leanback.widget.ImageCardView;

import com.castify.tv.R;
import com.castify.tv.models.PlayList;

/**
 * A very basic {@link ImageCardView} {@link androidx.leanback.widget.Presenter}.You can
 * pass a custom style for the ImageCardView in the constructor. Use the default constructor to
 * create a Presenter with a default ImageCardView style.
 */
public class CatViewPresenter extends AbstractCatPresenter<CategoryView> {
    private static final int MAX_LENGTH = 60;

    public CatViewPresenter(Context context, int cardThemeResId, String categoryType) {
        super(new ContextThemeWrapper(context, cardThemeResId), categoryType);
    }
    public CatViewPresenter(Context context, Activity activity, String categoryType) {
        this(context, R.style.DefaultCardTheme, categoryType);
    }


    @Override
    protected CategoryView onCreateView() {
        return null;
    }

    @Override
    public void onBindViewHolder(PlayList playList, CategoryView cardView) {
        cardView.setCategoryThumbnail(playList.getImage());
        cardView.setCategoryTitle(playList.getName());
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