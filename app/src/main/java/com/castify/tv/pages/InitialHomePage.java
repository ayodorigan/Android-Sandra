package com.castify.tv.pages;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.castify.tv.R;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.models.VideoCard;
import com.castify.tv.rowvideos.InitialHomeVideos;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InitialHomePage extends Fragment {

    // TextViews
    public TextView title, video_description;

    // Layouts
    public LinearLayout moreDetailsTxt, detailedBackgroundHolder, detailedDescription, videoStatistics, homeVideosHolder;
    public FrameLayout  homeVideos;


    // Images
    public ImageView appLogo;
    public ImageView detailedBackground;

    int MAX_LENGTH = 197;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView =  inflater.inflate(R.layout.home, container, false);
        title = homeView.findViewById(R.id.video_title);
        video_description = homeView.findViewById(R.id.video_description);
        moreDetailsTxt = homeView.findViewById(R.id.moredetailstext);
        homeVideos = homeView.findViewById(R.id.homeVideos);
        homeVideosHolder = homeView.findViewById(R.id.homeVideosHolder);
        detailedBackgroundHolder = homeView.findViewById(R.id.detailedBackgroundHolder);
        detailedDescription = homeView.findViewById(R.id.detailedDescription);
        videoStatistics = homeView.findViewById(R.id.titleViews);
        detailedBackground = homeView.findViewById(R.id.detailedBackground);
        appLogo = homeView.findViewById(R.id.appLogoV4);


        FontStyles.setFontArimoBold(requireContext(), title, false);
        FontStyles.setFontArimoBold(requireContext(), video_description, false);


        FadingEdgeLayout mFadingEdgeLayout = homeView.findViewById(R.id.fading_edge_layout);
        mFadingEdgeLayout.setFadeEdges(false, true, true, false);
        mFadingEdgeLayout.setFadeSizes(0, 600, 600, 0);

        android.transition.Slide transition =  new android.transition.Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        TransitionManager.beginDelayedTransition(detailedBackgroundHolder, transition);
        PageModel pageModel = null;
        ArrayList<PlayList> homePlayList = null;

        if (!GlobalVars.showMenu) {
            appLogo.setVisibility(View.VISIBLE);
            GlobalFuncs.loadImage(GlobalVars.graphics.getAppLogo(), requireContext(), appLogo);
            if (GlobalVars.graphics.getAppLogo() != null && !GlobalVars.graphics.getAppLogo().isEmpty()) {
                GlobalFuncs.loadImage(GlobalVars.graphics.getAppLogo(), requireContext(), appLogo);
            } else if (GlobalVars.graphics.getSplashScreen() != null && !GlobalVars.graphics.getSplashScreen().isEmpty()) {
                GlobalFuncs.loadImage(GlobalVars.graphics.getSplashScreen(), requireContext(), appLogo);
            } else {
                appLogo.setVisibility(View.GONE);
            }
        }else{
            Bundle bundle = getArguments();
            assert bundle != null;
            pageModel = bundle.getParcelable(GlobalVars.currentPageModelTag);

            homeView.findViewById(R.id.appLogoV4).setVisibility(View.GONE);
            PlayList homeContainer = GlobalFuncs.getContainer(pageModel.getPage_id());
            if (homeContainer != null) {
                List<PlayList> homeCats = GlobalFuncs.getContainerCategories(homeContainer);
                homePlayList = new ArrayList<>();
                homePlayList.add(homeContainer);
                homePlayList.addAll(homeCats);


            }
        }



        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.homeVideos, new InitialHomeVideos(this, homePlayList, pageModel));
        ft.commit();
        homeVideos.requestFocus();
        return homeView;

    }

    public void toggleDetailed(boolean showDetailed) {
        if (showDetailed) {
            if (detailedDescription.getVisibility() == View.VISIBLE)  {
                return;
            }
            detailedBackgroundHolder.setVisibility(View.VISIBLE);
            detailedDescription.setVisibility(View.VISIBLE);
            homeVideosHolder.setPaddingRelative(0,20, 0, 0);
            return;
        }
        if (detailedDescription.getVisibility() == View.GONE)  {
            return;
        }


        detailedBackgroundHolder.setVisibility(View.GONE);
        detailedDescription.setVisibility(View.GONE);
        if (appLogo.getVisibility() == View.VISIBLE) {
            homeVideosHolder.setPaddingRelative(0,200, 0, 0);
            return;
        }
        homeVideosHolder.setPaddingRelative(0,40, 0, 0);

    }


    public void setTopPanelDetails(VideoCard videoCard) {
        String video_description = videoCard.getDescription();
        String title = videoCard.getTitle();

        if (videoCard.getDescription().length() > MAX_LENGTH ) {
            video_description = video_description.substring(0, MAX_LENGTH);
            video_description = video_description + "...";
        }
        title = title  + "";
        this.title.setText(title);
        if (moreDetailsTxt.getVisibility() == View.VISIBLE){
            this.video_description.setText(video_description);
        }
        showVideoStatistics(videoCard);
    }

    public  void showVideoStatistics(VideoCard videoCard) {

        videoStatistics.removeAllViews();

        // Show video views
        if (GlobalFuncs.checkString(videoCard.getViews())!= null && Integer.parseInt(videoCard.getViews()) > 0) {
            videoStatistics.addView(statisticsView(ContextCompat.getDrawable(requireContext(), R.drawable.icon_review), formatCounts(videoCard.getViews())));
        }

        // Show Video likes
        if (GlobalFuncs.checkString(videoCard.getLikes())!= null && Integer.parseInt(videoCard.getLikes()) > 0) {
            videoStatistics.addView(statisticsView(ContextCompat.getDrawable(requireContext(),R.drawable.icon_likes), formatCounts(videoCard.getLikes())));
        }

        // Show the category where the video belongs
        if (GlobalFuncs.checkString(videoCard.getCategory())!= null) {
            videoStatistics.addView(statisticsView(ContextCompat.getDrawable(requireContext(),R.drawable.icon_category), videoCard.getCategory()));
        }



        // show the duration of the video
        if (videoCard.isIs_live_streaming()) {
            videoStatistics.addView(statisticsView(ContextCompat.getDrawable(requireContext(),R.drawable.icon_duration_video), "Live"));
        } else  {
            // show the duration of the video
            if (GlobalFuncs.checkString(videoCard.getVideoDuration())!= null) {
                videoStatistics.addView(statisticsView(ContextCompat.getDrawable(requireContext(),R.drawable.icon_duration_video), GlobalFuncs.getDurationString(Integer.parseInt(videoCard.getVideoDuration()))));
            }
        }


        // Only show three view and if they 4 or more remove the trailing one
        if (videoStatistics.getChildCount() >= 4) {
            videoStatistics.removeViewAt(videoStatistics.getChildCount() - 1);
        }
    }

    View statisticsView(Drawable icon, String value){
        TextView textView = new TextView(requireContext());
        textView.setText(value);
        textView.setTextColor(Color.parseColor("#ECF0F1"));
        textView.setTextSize(12);
        textView.setMaxLines(1);
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textViewLayoutParams);
        FontStyles.setFontArimoBold(requireContext(), textView, false);
        if (value.equals("Live")) {
            textView.setTextColor(Color.RED);

        }
        ImageView imageView = new ImageView(requireContext());
        imageView.setImageDrawable(icon);
        imageView.setPadding(0, 0, 5, 0);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
        imageView.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0,0,20,0);
        layout.addView(imageView);
        layout.addView(textView);


        return layout;
    }


    private String formatCounts(String counts) {
        try {
            float count = Float.parseFloat(counts);
            DecimalFormat df = null;
            String noType = "";
            if (count >= 1000 && count < 10000){
                count = count / 1000;
                df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.DOWN);
                noType = "K";
            }

            if (count >= 10000 && count < 1000000){
                count = count / 10000;
                df = new DecimalFormat("#");
                noType =  "K";
            }

            if (count >= 1000000 && count < 10000000){
                count = count / 1000000;
                df = new DecimalFormat("#.#");
                noType = "M";
            }

            if (count >= 10000000){
                count = count / 10000000;
                df = new DecimalFormat("#");
                noType = "M";
            }
            if (df != null) {
                df.setRoundingMode(RoundingMode.DOWN);
                return df.format(count) + noType;
            }

        }catch (Exception e) {
            Log.e(getClass().getName(), "Count Formatting error", e);
        }
        return counts;
    }


}
