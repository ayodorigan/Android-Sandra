package com.castify.tv.presenters;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.Presenter;

import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.VideoCard;

import java.util.ArrayList;

/**
 * This abstract, generic class will create and manage the
 * ViewHolder and will provide typed Presenter callbacks such that you do not have to perform casts
 * on your own.
 *
 * @param <T> View type for the card.
 */
public abstract class AbstractCardPresenter<T extends BaseCardView> extends Presenter {

    private final Context mContext;

    /**
     * @param context The current context.
     */
    public AbstractCardPresenter(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override public final ViewHolder onCreateViewHolder(ViewGroup parent) {
        VideoCardPresenter cardView = new VideoCardPresenter(parent.getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 0, 10, 0);
        cardView.setLayoutParams(params);
        cardView.setBackgroundColor(Color.TRANSPARENT);
        GlobalVars.allVideoCategories =  new ArrayList<>(GlobalVars.videos.keySet());
        return new ViewHolder(cardView);
    }

    @Override public final void onBindViewHolder(ViewHolder viewHolder, Object item) {
        VideoCard videoCard = (VideoCard) item;
//        int position = GlobalVars.allVideoCategories.indexOf(videoCard.getCategory()) -1;
//        Animation animation = AnimationUtils.loadAnimation(getContext(),
//                (position > lastPosition) ? R.anim.scroll_up_from_bottom
//                        : R.anim.scroll_down_from_top);
//        viewHolder.view.startAnimation(animation);
//        lastPosition = position;
        onBindViewHolder(videoCard, (T) viewHolder.view);

    }

    @Override public final void onUnbindViewHolder(ViewHolder viewHolder) {
        onUnbindViewHolder((T) viewHolder.view);
    }

    public void onUnbindViewHolder(T cardView) {
        // Nothing to clean up. Override if necessary.
    }


    /**
     * Invoked when a new view is created.
     *
     * @return Returns the newly created view.
     */
    protected abstract T onCreateView();

    /**
     * Implement this method to update your card's view with the data bound to it.
     *
     * @param videoCard The Models containing the data for the card.
     * @param cardView The view the card is bound to.
     * @see Presenter#onBindViewHolder(ViewHolder, Object)
     */
    public abstract void onBindViewHolder(VideoCard videoCard, T cardView);

}
