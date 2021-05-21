
package com.castify.tv.presenters;

import android.app.Activity;
import android.content.Context;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.castify.tv.models.VideoCard;

/**
 * This PresenterSelector will decide what Presenter to use depending on a given card's type.
 */
public class CardPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private  Activity activity;

    public CardPresenterSelector(Context context, Activity activity) {
        mContext = context;
        this.activity = activity;
    }


    @Override
    public Presenter getPresenter(Object item) {
        if (!(item instanceof VideoCard)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        VideoCard.class.getSimpleName()));
        Presenter presenter  = new ImageCardViewPresenter(mContext, null);;

        return presenter;
    }


}
