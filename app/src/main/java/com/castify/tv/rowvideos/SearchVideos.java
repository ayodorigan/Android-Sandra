

package com.castify.tv.rowvideos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PlayList;
import com.castify.tv.presenters.CustomVerticalGridPresenter;
import com.castify.tv.presenters.ImageCardViewPresenter;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.SendLogs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.VideoCard;
import com.castify.tv.activities.PlayerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchVideos extends VerticalGridSupportFragment {
    final int COL_NUM = 4;
    private static final String TAG = SearchVideos.class.getSimpleName();
    private ArrayObjectAdapter mAdapter;
    PageModel pageModel;
    EditText searchInput;
    TextView searchResultCount;
    PageGraphics pageGraphics;
    View focusView;
    boolean focus;

    public SearchVideos(View focusView, EditText searchInput, TextView searchResultCount, PageModel pageModel) {
        this.searchInput = searchInput;
        this.pageModel = pageModel;
        this.focusView = focusView;
        this.searchResultCount = searchResultCount;
        pageGraphics = pageModel.getGraphic();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRowAdapter();
        setupEventListeners();
        for (PageModel pageModel : GlobalVars.pages) {
            if (pageModel.getMenu_title().equalsIgnoreCase("searchPage")) {
                this.pageModel = pageModel;
                break;
            }
        }

    }

    @Override
    public void setSelectedPosition(int position) {
        super.setSelectedPosition(position);
    }

    private void setupRowAdapter() {
        CustomVerticalGridPresenter gridPresenter = new CustomVerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false);
        gridPresenter.setShadowEnabled(false);
        gridPresenter.setNumberOfColumns(COL_NUM);
        setGridPresenter(gridPresenter);

        mAdapter = new ArrayObjectAdapter(new ImageCardViewPresenter(this.requireContext() , pageGraphics));
        mAdapter.setHasStableIds(false);

        setAdapter(mAdapter);
        loadRows();

        final Handler handlerUI = new Handler(Looper.getMainLooper());
        Runnable r = () -> searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                search(s.toString());
            }
        });
        handlerUI.post(r);

    }

    public void search(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            loadRows();
            return;
        }

        if (keyword.length() < 2) {
            searchResultCount.setText(null);
            return;
        }

        List<VideoCard> filteredArticleList = (Stream.of(GlobalVars.allVideos).filter(videoCard -> videoCard.getTitle().toLowerCase().contains(keyword.toLowerCase()))).collect(Collectors.toList());
        if (filteredArticleList == null) {
            return;
        }

        int resultsCounts = filteredArticleList.size();
        mAdapter.notifyItemRangeChanged(0, mAdapter.size());
        String results = "";

        mAdapter.clear();
        if (resultsCounts <= 0 ) {
            results =" No Matching Videos";
        } else {
            mAdapter.addAll(0, filteredArticleList);
            if (resultsCounts == 1) {
                results = resultsCounts + " Video";
            } else {
                results = resultsCounts + "  Videos";
            }
        }
        searchResultCount.setText(results);
    }

    private void loadRows() {
       try {
           for (Map.Entry<PlayList, ArrayList<VideoCard>> category  : GlobalVars.videos.entrySet()) {
               mAdapter.addAll(0, category.getValue());
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

    public final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof VideoCard) {
                Intent intent = new Intent(requireActivity(), PlayerActivity.class);
                intent.putExtra(GlobalVars.selectedVideoToPlayTag, (VideoCard)item);
                PlayList homeContainer = GlobalFuncs.getContainer(134);
                if (homeContainer != null) {
                    List<PlayList> homeCats = GlobalFuncs.getContainerCategories(homeContainer);
                    ArrayList<PlayList> categories = new ArrayList<>();
                    categories.add(homeContainer);
                    categories.addAll(homeCats);
                    intent.putExtra(GlobalVars.categoriesTag, categories);
                }
                intent.putExtra(GlobalVars.currentPageModelTag, pageModel);
                intent.putExtra(GlobalVars.searchKeyWord, String.valueOf(searchInput.getText()).trim());
                startActivity(intent);
                new SendLogs(SearchVideos.this.requireContext(), GlobalVars.CAROUSEL_CLICK, (VideoCard)item,  pageModel ).start();
            }
        }
    }

    private  final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (!(item instanceof VideoCard)) return;

            VideoCard videoCard = (VideoCard)item;

            itemViewHolder.view.setOnKeyListener((view, i, keyEvent) -> {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                        return false;
                    }
                    int videoIndex = mAdapter.indexOf(videoCard);
                    if (videoIndex < COL_NUM && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        focusView.requestFocus();
                        Log.e("videoc", videoIndex + " - " + videoCard.getTitle());
                        return true;
                    }
                }
                return false;
            });
        }
    }
}
