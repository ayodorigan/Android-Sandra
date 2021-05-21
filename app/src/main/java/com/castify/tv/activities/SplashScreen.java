package com.castify.tv.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.castify.tv.R;
import com.castify.tv.models.AdModel;
import com.castify.tv.models.FeedInfo;
import com.castify.tv.models.Graphics;
import com.castify.tv.models.Info;
import com.castify.tv.models.MenuBar;
import com.castify.tv.models.MenuPageItem;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.models.RawFeedInfo;
import com.castify.tv.models.VideoCard;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.utils.HttpConnect;
import com.castify.tv.utils.Network;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static com.castify.tv.dialogs.ErrorDialogs.genError;
import static com.castify.tv.dialogs.ErrorDialogs.invalidFeedError;
import static com.castify.tv.dialogs.ErrorDialogs.noInternetError;

public class SplashScreen extends FragmentActivity  {

    private final String TAG = getClass().getSimpleName();

    private LinearLayout spinner;
    private ImageView splashImage;

    Gson gson = new Gson();

    String catalogURL;
    String splashURL;
    boolean splashScreenShown;

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        splashImage = findViewById(R.id.splash_image);

        spinner = findViewById(R.id.loadingDots);
        TextView appVersion = findViewById(R.id.appVersion);
        FontStyles.setFontArimoBold(this, appVersion, false);

        Animation appVersionAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        appVersion.setText( GlobalVars.APP_VERSION);
        appVersion.startAnimation(appVersionAnimation);

        catalogURL = GlobalVars.CHANNEL_URL;
        splashURL = catalogURL.split(".json")[0] + "&cad[channel_output]=_splash";
        new start(this).execute();

    }


    private static class start extends AsyncTask<Void, Void, Boolean> {
        @SuppressLint("StaticFieldLeak")
        SplashScreen splashScreen;
        start(SplashScreen splashScreen) {
            this.splashScreen = splashScreen;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected Boolean doInBackground(Void... voids) {
            if (!new Network().isNetwork(splashScreen.getBaseContext())) {
                return false;
            }
            String feedURL = GlobalVars.CHANNEL_URL;
            if (!feedURL.contains("TIMESTAMP")) { // Check if the feed is a new one else
                // support old version feed without the manifest
                FeedInfo feedInfo = new FeedInfo();
                feedInfo.setContent_feed_url(feedURL);
                feedInfo.setSplash_screen(feedURL.replace(".json", "_splash.json"));
                GlobalVars.feedInfo = feedInfo;
                return true;

            }

            //support new feed with the manifest
            GlobalFuncs.extractMacros(splashScreen);

            feedURL = GlobalFuncs.setMacros(feedURL, null, null, null);
            JSONObject resData;
            try {
                resData = HttpConnect.fetchJSON(feedURL);
                if (resData != null && resData.has("finalClickUrl")) {
                    RawFeedInfo rawFeedInfo = splashScreen.gson.fromJson(resData.toString(), RawFeedInfo.class);

                    if (GlobalFuncs.checkString(rawFeedInfo.getfinalClickUrl()) != null) {
                        GlobalVars.rawFeedInfo = rawFeedInfo;
                        JSONObject feedInfoObj;
                        try {
                            feedInfoObj = HttpConnect.fetchJSON(GlobalVars.rawFeedInfo.getfinalClickUrl());
                            FeedInfo feedInfo = splashScreen.gson.fromJson(feedInfoObj.toString(), FeedInfo.class);
                            if (GlobalFuncs.checkString(feedInfo.getContent_feed_url())!= null) {
                                GlobalVars.feedInfo = feedInfo;
//                                Log.e("getContent_feed_url", GlobalFuncs.checkString(feedInfo.getContent_feed_url()));

                            }

                        } catch (Exception e) {
                            invalidFeedError(splashScreen, null, ()->splashScreen.finish());
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean internetExist) {

            super.onPostExecute(internetExist);

            if (internetExist) {
                new FetchVideoData(splashScreen).execute();
            } else  {
                noInternetError(splashScreen, null, ()->splashScreen.finish());
            }
        }
    }


    private static class FetchVideoData extends AsyncTask<Void, Void, String> {

        String TAG = getClass().getSimpleName();

        JSONArray playlistData;

        @SuppressLint("StaticFieldLeak")
        SplashScreen splashScreen;
        FetchVideoData(SplashScreen splashScreen) {
            this.splashScreen = splashScreen;
        }


        @SuppressLint("CheckResult")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (GlobalVars.feedInfo != null && GlobalVars.feedInfo.getSplash_screen() != null && !GlobalVars.feedInfo.getSplash_screen().isEmpty()) {
                    splashScreen.spinner.setVisibility(View.GONE);
                    Glide.with(splashScreen).load(GlobalVars.feedInfo.getSplash_screen()).into(splashScreen.splashImage);
                    splashScreen.splashScreenShown = true;
                }
            }catch ( Exception e) {
                Log.e(TAG, "Error", e);
                genError(splashScreen, null, ()->splashScreen.finish());
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Void... voids) {
            try {

                if (GlobalVars.feedInfo == null) {
                    return "error";
                }

                Gson gson = new Gson();

                JSONObject mainData = HttpConnect.fetchJSON(GlobalVars.feedInfo.getContent_feed_url());

                //Get Graphics info
                assert mainData != null;
                try {

                    if (mainData.has(GlobalVars.MENU_KEY)) {
                        GlobalVars.showMenu = true;
                        JSONObject menuData = mainData.getJSONObject(GlobalVars.MENU_KEY);
                        GlobalVars.menuBar = gson.fromJson(menuData.toString(), MenuBar.class);

                        JSONArray menuItems = menuData.getJSONArray("pages");

                        // Get menu item
                        if (menuItems.length() > 0) {

                            for (int x = 0; x < menuItems.length(); x++) {

                                JSONObject menu = menuItems.getJSONObject(x);

                                PageModel pageModel = gson.fromJson(menu.toString(), PageModel.class);

                                // Get Menu page items
                                ArrayList<MenuPageItem> menuPageItemsArray = new ArrayList<>();
                                if (menu.has("items")) {

                                    JSONArray menuPageItems = menu.getJSONArray("items");

                                    if (menuPageItems.length() > 0 ) {
                                        for (int c = 0; c < menuPageItems.length(); c++) {
                                            JSONObject menuPItem = menuPageItems.getJSONObject(c);
                                            MenuPageItem menuPageItem = gson.fromJson(menuPItem.toString(), MenuPageItem.class);

//                                        menuPageItem.setPage_id(pageModel.getPage_id());
//                                        menuPageItem.setPage_type_id(pageModel.getPage_type_id());
                                            menuPageItemsArray.add(menuPageItem);
                                        }

                                    }

                                }

                                if (!GlobalVars.menus.containsKey(pageModel)) {
                                    GlobalVars.menus.put(pageModel, menuPageItemsArray);
                                }

                            }

                        }

                    }


                }catch ( Exception e) {
                    Log.e(TAG, "Error", e);
                    genError(splashScreen, null, ()->splashScreen.finish());

                }

                try {
                    JSONObject graphicsData = mainData.getJSONObject(GlobalVars.GRAPHIC_key);

                    // Get Graphics Data
                    if (graphicsData.keys().hasNext()) {
                        GlobalVars.graphics = gson.fromJson(graphicsData.toString(), Graphics.class);

                    }

                }catch ( Exception e) {
                    Log.e(TAG, "Error", e);
                    genError(splashScreen, null, ()->splashScreen.finish());

                }


                try {
                    JSONObject infoData = mainData.getJSONObject(GlobalVars.INFO_key);

                    //Get info data
                    if (infoData.keys().hasNext()) {

                        GlobalVars.info = gson.fromJson(infoData.toString(), Info.class);

                    }

                }catch ( Exception e) {
                    Log.e(TAG, "Error", e);
                }

                //Get beacon URL
                try {
                    JSONObject beaconData = mainData.getJSONObject(GlobalVars.BEACON_key);
                    GlobalVars.BEACON_url = beaconData.getString("url");
                }catch (Exception e){
                    Log.e(TAG, "Error", e);
                }

                try {
                    JSONObject adData = mainData.getJSONObject(GlobalVars.AD_Key);

                    //Get Ads
                    if (adData.keys().hasNext()) {

                        GlobalVars.adModel = gson.fromJson(adData.toString(), AdModel.class);

                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                }


                try {
                    JSONArray videosData = mainData.getJSONArray(GlobalVars.CONTENT_key);
                    Type type = new TypeToken<ArrayList<VideoCard>>(){}.getType();
                    GlobalVars.allVideos = gson.fromJson(videosData.toString(), type); // get all the videos @ once

                    playlistData = mainData.getJSONArray(GlobalVars.PlaylistKey);


                    // Get Video Data
                    if (playlistData.length() > 0) {

                        for (int x = 0; x < playlistData.length(); x++) {

                            PlayList playList = gson.fromJson(playlistData.get(x).toString(), PlayList.class);
                            if (playList.getLevel_id() == 0 && playList.getType().equals("category")) {
                                GlobalVars.typeContainers.add(playList); // Get type containers
                            }

                            if (playList.getLevel_id() == 1 && playList.getType().equals("category")) {
                                GlobalVars.typeCategories.add(playList); // Get type categories
                            }

                            if (playList.getType().equals("contents")) {
                                GlobalVars.typeContents.add(playList); // Get type contents
                            }
                            GlobalVars.playList.add(playList);

                        }

                    }
                    GlobalVars.videos = GlobalFuncs.getContentVideos(GlobalVars.playList);

                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                }

                return "ok";

            } catch (Exception e) {

                Log.e(TAG, "JSON Error", e);
                return "error";


            }
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if (s.equals("error")){
                invalidFeedError(splashScreen, null, ()->System.exit(1));
                return;
            }
            if (GlobalVars.graphics != null && GlobalFuncs.hasValue(GlobalVars.graphics.getHomeScreen()) && Integer.parseInt(GlobalVars.graphics.getHomeScreen()) > 0) {
                // Display InitialHomePage screen
                Intent mainIntent = new Intent(splashScreen, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                GlobalVars.hasHomeScreen = true;
                splashScreen.startActivity(mainIntent);
                splashScreen.finish();

            } else  {
                // Go direct to video Player
                Intent intent = new Intent(splashScreen, PlayerActivity.class);

                for (Map.Entry<PlayList, ArrayList<VideoCard>> videos: GlobalVars.videos.entrySet()) {

                    for (VideoCard videoCard: videos.getValue()) {

                        try {

                            if (videoCard !=null && videoCard.getTitle() != null) {
                                intent.putExtra(GlobalVars.selectedVideoToPlayTag, videoCard);
                                break;
                            }

                        }catch (Exception e) {
                            Log.e(TAG, "ERROR PLAYBACK", e);
                        }

                    }
                    break;
                }

                splashScreen.startActivity(intent);

            }

            splashScreen.finish();

        }


    }

}