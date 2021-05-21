
package com.castify.tv.presenters;

import android.app.Activity;
import android.content.Context;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.castify.tv.models.PlayList;
import com.castify.tv.models.VideoCard;

/**
 * This PresenterSelector will decide what Presenter to use depending on a given card's type.
 */
public class CatPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private  String categoryType;

    private  Activity activity;


    public CatPresenterSelector(Context context, Activity activity, String categoryType) {
        mContext = context;
        this.activity = activity;
        this.categoryType = categoryType;
    }


    @Override
    public Presenter getPresenter(Object item) {
        if (!(item instanceof PlayList)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        VideoCard.class.getSimpleName()));
        Presenter presenter  = new CatViewPresenter(mContext, activity, categoryType);;

        return presenter;
    }


}
