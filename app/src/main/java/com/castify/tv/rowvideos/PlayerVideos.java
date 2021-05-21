

package com.castify.tv.rowvideos;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PlayList;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.SendLogs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.VideoCard;
import com.castify.tv.presenters.CustomRowHeaderPresenter;
import com.castify.tv.presenters.ImageCardViewPresenter;
import com.castify.tv.activities.PlayerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerVideos extends RowsSupportFragment {

    private static final String TAG = PlayerVideos.class.getSimpleName();

    private ArrayObjectAdapter mAdapter;
    private Map<PlayList, ArrayList<VideoCard>> videos;
    PageModel pageModel;

    View playBtn, pauseBtn;
    ListRow firstRow;
    ArrayList<VideoCard> firstRowVideos = new ArrayList<>();
    PageGraphics pageGraphics;
    View topControls, customControlPanel;
    public PlayerVideos(View playBtn, View pauseBtn, Map<PlayList, ArrayList<VideoCard>> videos, PageModel pageModel, View topControls, View customControlPanel) {
        this.playBtn = playBtn;
        this.pauseBtn = pauseBtn;

        this.videos = videos;
        this.topControls = topControls;
        this.customControlPanel = customControlPanel;
        if (pageModel != null) {
            this.pageModel = pageModel;
        }
        this.pageGraphics = new PageGraphics();
        this.pageGraphics.setTitle_color("#ffffff");
        this.pageGraphics.setText_color("#ffffff");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEventListeners();
        setAlignment(getResources().getDimensionPixelOffset(R.dimen.lb_browse_padding_top));
        Bundle bundle = requireActivity().getIntent().getExtras();
        assert bundle != null;
        setupRowAdapter();
    }


    private void setupRowAdapter() {
        mAdapter = new ArrayObjectAdapter(new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {
            @Override
            public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item, List<Object> payloads) {
                super.onBindViewHolder(viewHolder, item, payloads);
                viewHolder.view.setNextFocusUpId(playBtn.getNextFocusUpId());

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                super.createRowViewHolder(parent);

                ListRowView rowView = new ListRowView(parent.getContext());
                rowView.getGridView().setWindowAlignment(1);
                HorizontalGridView view = rowView.getGridView();
                view.setWindowAlignment( VerticalGridView.WINDOW_ALIGN_NO_EDGE);
                view.setWindowAlignmentOffsetPercent(GlobalVars.playerWindowAlignmentOffsetPercent);
                setSelectEffectEnabled(false);
                setHeaderPresenter(new CustomRowHeaderPresenter(pageGraphics, 0));
                return new ViewHolder(rowView, rowView.getGridView(), this);
            }
        });

        setAdapter(mAdapter);
        loadRows();
    }



    private void loadRows() {
       try {
           int index = 0;
           for (Map.Entry<PlayList, ArrayList<VideoCard>> category  : videos.entrySet()) {
               HeaderItem cardPresenterHeader = new HeaderItem(index++, (category.getKey()).getName() );
               ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(new ImageCardViewPresenter(requireActivity(), pageGraphics));
               cardRowAdapter.addAll(0, category.getValue());
               mAdapter.add(new ListRow(cardPresenterHeader, cardRowAdapter));
           }
           firstRow = (ListRow)mAdapter.get(0);
           for (int x = 0; x < firstRow.getAdapter().size(); x++) {
               VideoCard videoCard = (VideoCard)firstRow.getAdapter().get(x);
               firstRowVideos.add(videoCard);
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
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof VideoCard) {
                ((PlayerActivity) requireContext()).playVideo((VideoCard)item);
                new SendLogs(PlayerVideos.this.requireContext(), GlobalVars.CAROUSEL_CLICK, (VideoCard)item,  pageModel).start();
            }

        }
    }

    private  final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            requireActivity().runOnUiThread(()-> {
                if (item instanceof VideoCard) {
                    VideoCard videoCard = (VideoCard)item;
                }

                VideoCard videoCard = null;
                if (item instanceof VideoCard) {
                    videoCard = (VideoCard)item;
                }

                if ( itemViewHolder == null) {
                    return;
                }
                VideoCard finalVideoCard = videoCard;
                itemViewHolder.view.setOnKeyListener((view, keyCode, keyEvent) -> {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                            return false;
                        }
                        if ( finalVideoCard != null && firstRowVideos.contains(finalVideoCard)  && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            customControlPanel.setPadding(0, 650,0, 0);
                            topControls.setVisibility(View.VISIBLE);
                            if (pauseBtn.getVisibility() == View.VISIBLE){
                                pauseBtn.requestFocus();
                            }
                            if (playBtn.getVisibility() == View.VISIBLE){
                                playBtn.requestFocus();
                            }
                            return true;
                        }
                    }
                    return false;
                });

//                if (  videoCard != null && firstRowVideos.contains(videoCard) && itemViewHolder != null) {
//
//                    itemViewHolder.view.setOnKeyListener((view, keyCode, keyEvent) -> {
//                        if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN) {
//                            return true;
//                        }
//                        if ( keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//                            customControlPanel.setPadding(0, 650,0, 0);
//                            topControls.setVisibility(View.VISIBLE);
//                            if (pauseBtn.getVisibility() == View.VISIBLE){
//                                pauseBtn.requestFocus();
//                            }
//                            if (playPauseBtn.getVisibility() == View.VISIBLE){
//                                playPauseBtn.requestFocus();
//                            }
//                            return true;
//                        }
//                        return false;
//                    });
//                }
            });
        }
    }
}
