package com.castify.tv.pages;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PlayList;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.models.PageModel;
import com.castify.tv.rowvideos.LiveVideos;
import com.castify.tv.utils.GlobalVars;

import java.util.ArrayList;
import java.util.Objects;

public class LivePage extends Fragment  {

    // Text views
    public TextView liveCatTitle;
    public TextView liveCatDesc;
    public ImageView categoryThumbnail;
    public LinearLayout liveVideosContainer;

    ArrayList<PlayList> pageCategories = new ArrayList<>();
    PageGraphics pageGraphics ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.menu_live, container, false);

        // Initialize
        liveCatTitle = view.findViewById(R.id.liveCatTitle);
        liveCatDesc = view.findViewById(R.id.liveCatDesc);
        categoryThumbnail = view.findViewById(R.id.categoryThumbnail);
        liveVideosContainer = view.findViewById(R.id.liveVideosContainer);
        FontStyles.setFontArimoBold(requireContext(), liveCatDesc, false);
        FontStyles.setFontArimoBold(requireContext(), liveCatTitle, false);

        //Get PageModel
        Bundle bundle = getArguments();
        assert bundle != null;
        PageModel pageModel = bundle.getParcelable(GlobalVars.currentPageModelTag);
        if (pageModel == null ) {
            return view;
        }
        pageGraphics = pageModel.getGraphic();
        GlobalFuncs.setUpPageTitle(view, pageModel, requireContext(), false, pageGraphics);

        if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ){
            liveCatDesc.setTextColor(Color.parseColor(pageGraphics.getText_color()));
            liveCatTitle.setTextColor(Color.parseColor(pageGraphics.getText_color()));
        }


        PlayList catPageContainer = GlobalFuncs.getContainer(pageModel.getPage_id());
        pageCategories.add(catPageContainer);
        pageCategories.addAll(Objects.requireNonNull(GlobalFuncs.getContainerCategories(catPageContainer)));

        FragmentManager manager = getParentFragmentManager();
        Fragment fragment = new LiveVideos(pageCategories, pageModel);
        manager.beginTransaction().add(R.id.liveVideosContainer, fragment).commit();
        liveVideosContainer.requestFocus();
        return view;
    }

    public TextView getLiveCatTitle() {
        return liveCatTitle;
    }

    public void setLiveCatTitle(TextView liveCatTitle) {
        this.liveCatTitle = liveCatTitle;
    }

    public TextView getLiveCatDesc() {
        return liveCatDesc;
    }

    public void setLiveCatDesc(TextView liveCatDesc) {
        this.liveCatDesc = liveCatDesc;
    }

    public ImageView getCategoryThumbnail() {
        return categoryThumbnail;
    }

    public void setCategoryThumbnail(ImageView categoryThumbnail) {
        this.categoryThumbnail = categoryThumbnail;
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }
}
