

package com.castify.tv.rowvideos;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ListRowView;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridView;

import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PlayList;
import com.castify.tv.pages.InitialHomePage;
import com.castify.tv.R;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.SendLogs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.VideoCard;
import com.castify.tv.presenters.CustomRowHeaderPresenter;
import com.castify.tv.presenters.ImageCardViewPresenter;
import com.castify.tv.activities.MainActivity;
import com.castify.tv.activities.PlayerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InitialHomeVideos extends RowsSupportFragment {
    private static final String TAG = InitialHomeVideos.class.getSimpleName();
    private ArrayObjectAdapter mAdapter;
    private Map<PlayList, ArrayList<VideoCard>> videos;
    ArrayList<PlayList> categories;
    PageModel pageModel;
    InitialHomePage initialHomePage;
    MainActivity mainActivity;
    PageGraphics pageGraphics;
    public InitialHomeVideos(InitialHomePage initialHomePage, ArrayList<PlayList> categories, PageModel pageModel) {
        this.categories = categories;
        if (categories != null) {
            List<PlayList> categoryContents = GlobalFuncs.getCategoryContents(categories);
            videos = GlobalFuncs.getContentVideos(categoryContents);
        } else  {
            videos = GlobalVars.videos;
        }
        this.initialHomePage = initialHomePage;
        if (pageModel != null) {
            this.pageModel = pageModel;
            pageGraphics = this.pageModel.getGraphic();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRowAdapter();
        setupEventListeners();
        setAlignment(getResources().getDimensionPixelOffset(R.dimen.lb_browse_padding_top));
        mainActivity = (MainActivity)requireActivity();
    }


    private void setupRowAdapter() {
        mAdapter = new ArrayObjectAdapter(new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {
            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                super.createRowViewHolder(parent);
                ListRowView rowView = new ListRowView(parent.getContext());
                rowView.getGridView().setWindowAlignment(1);
                HorizontalGridView view = rowView.getGridView();
                view.setWindowAlignment( VerticalGridView.WINDOW_ALIGN_NO_EDGE);
                view.setWindowAlignmentOffsetPercent(GlobalVars.windowAlignmentOffsetPercent);
                setSelectEffectEnabled(false);
                setHeaderPresenter(new CustomRowHeaderPresenter(pageGraphics, GlobalVars.headerPaddingStart));
                return new ViewHolder(rowView, rowView.getGridView(), this);
            }
        });

        setAdapter(mAdapter);
        loadRows();
    }



    private void loadRows() {

       try {

           for (Map.Entry<PlayList, ArrayList<VideoCard>> category  : videos.entrySet()) {
               HeaderItem cardPresenterHeader = new HeaderItem(Integer.parseInt(category.getKey().getEntity_id()), (category.getKey()).getName() );
               ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(new ImageCardViewPresenter(requireActivity(), pageGraphics));
               ArrayList<VideoCard> videoCards = category.getValue();
               for (VideoCard videoCard : videoCards ){
                   videoCard.setDetailedCarousel(category.getKey().isDetailedCarousel());
                   videoCard.setCategory(category.getKey().getName());
                   cardRowAdapter.add(videoCard);
               }
               mAdapter.add(new ListRow(cardPresenterHeader, cardRowAdapter));
           }
       } catch (Exception e) {
           Log.e(TAG, "Error", e);
       }
    }

    private void setupEventListeners() {

        //When an Item is clicked
        setOnItemViewClickedListener(new ItemViewClickedListener());

        //When an Item is selected
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }




    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof VideoCard) {
                Intent intent = new Intent(requireActivity(), PlayerActivity.class);
                intent.putExtra(GlobalVars.selectedVideoToPlayTag, (VideoCard)item);
                intent.putExtra(GlobalVars.currentPageModelTag, pageModel);
                intent.putExtra(GlobalVars.categoriesTag, categories);
                startActivity(intent);
                new SendLogs(InitialHomeVideos.this.requireContext(), GlobalVars.CAROUSEL_CLICK, (VideoCard)item, pageModel).start();
            }


        }
    }

    private  final class ItemViewSelectedListener implements OnItemViewSelectedListener {

        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            requireActivity().runOnUiThread(()-> {
                if (item instanceof VideoCard) {
                    VideoCard videoCard = (VideoCard)item;
                    if (videoCard.isDetailedCarousel()) {
                        initialHomePage.toggleDetailed(true);
                        initialHomePage.setTopPanelDetails(videoCard);
                        mainActivity.bgShadow.setVisibility(View.INVISIBLE);
                        if (GlobalVars.graphics.isFully_detailed() != null && Boolean.parseBoolean(GlobalVars.graphics.isFully_detailed())) {
                            mainActivity.mBackgroundURI = Uri.parse((videoCard.getThumbnail()));
                            mainActivity.setBackground(videoCard.getThumbnail());
                        } else {
                            GlobalFuncs.loadImage(videoCard.getThumbnail(), requireContext(), initialHomePage.detailedBackground);
                            mainActivity.setBackground(null);
                        }
                    } else {
                        initialHomePage.toggleDetailed(false);
                        if (GlobalFuncs.hasValue(GlobalVars.graphics.getBackgroundImg())) {
                            mainActivity.setBackground(GlobalVars.graphics.getBackgroundImg());
                            mainActivity.bgShadow.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }
}
