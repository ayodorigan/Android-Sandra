package com.castify.tv.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.ads.identifier.AdvertisingIdClient;
import androidx.ads.identifier.AdvertisingIdInfo;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.castify.tv.R;
import com.castify.tv.activities.TVDialogActivity;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.models.VideoCard;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.castify.tv.utils.GlobalVars.ADS_TRACKING;
import static com.castify.tv.utils.GlobalVars.APP_VERSION;
import static com.castify.tv.utils.GlobalVars.ARG_DESC_RES;
import static com.castify.tv.utils.GlobalVars.ARG_ICON_RES;
import static com.castify.tv.utils.GlobalVars.ARG_NEGATIVE_RES;
import static com.castify.tv.utils.GlobalVars.ARG_POSITIVE_RES;
import static com.castify.tv.utils.GlobalVars.ARG_TITLE_RES;
import static com.castify.tv.utils.GlobalVars.COUNTRY;
import static com.castify.tv.utils.GlobalVars.DEVICE_ID;
import static com.castify.tv.utils.GlobalVars.DEVICE_INFO;
import static com.castify.tv.utils.GlobalVars.HEIGHT;
import static com.castify.tv.utils.GlobalVars.IDFA;
import static com.castify.tv.utils.GlobalVars.IFA_TYPE;
import static com.castify.tv.utils.GlobalVars.LANGUAGE;
import static com.castify.tv.utils.GlobalVars.MAC_ADDRESS;
import static com.castify.tv.utils.GlobalVars.REQUEST_CODE;
import static com.castify.tv.utils.GlobalVars.USER_AGENT;
import static com.castify.tv.utils.GlobalVars.USER_IP;
import static com.castify.tv.utils.GlobalVars.WIDTH;
import static com.castify.tv.utils.GlobalVars.adModel;
import static com.castify.tv.utils.GlobalVars.allVideos;
import static com.castify.tv.utils.GlobalVars.graphics;
import static com.castify.tv.utils.GlobalVars.info;
import static com.castify.tv.utils.GlobalVars.menuBar;
import static com.castify.tv.utils.GlobalVars.menus;
import static com.castify.tv.utils.GlobalVars.typeContainers;
import static com.castify.tv.utils.GlobalVars.typeContents;

public class GlobalFuncs {

    public static String TAG = "GlobalFuncs";

    public static void setAppLogo(String logoURL, Context context, ImageView imageView) {
        try {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.screen_shadow);

            Glide.with(context)
//                    .asBitmap()
                    .load(logoURL)
                    .apply(options)
                    .into(imageView);


        }catch (Exception e) {
            Log.e(GlobalFuncs.class.getSimpleName(), "App Logo Error:", e);

        }

    }


    public static void loadImage(String logoURL, Context context, ImageView imageView) {
        try {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(10f);
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#FFFFFF"), Color.parseColor("#000000"));
            circularProgressDrawable.start();

            RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
            requestBuilder.load(GlobalVars.graphics.getDefaultThumbnail());
            GlideApp.with(context)
                    .asBitmap()
                    .load(logoURL)
                    .error(requestBuilder)
                    .placeholder(circularProgressDrawable)
                    .into(imageView);
        }catch (Exception e) {
            Log.e(GlobalFuncs.class.getSimpleName(), "App Logo Error:", e);

        }

    }


    public static void setUpPageTitle(View view, PageModel pageModel, Context context, boolean setSideImage, PageGraphics pageGraphics) {
        TextView appName = view.findViewById(R.id.appName);
        TextView separator = view.findViewById(R.id.separator);
        TextView pageTile = view.findViewById(R.id.pageTile);

        FontStyles.setFontArimoBold(context, separator, false);
        FontStyles.setFontArimoBold(context, appName, true);
        FontStyles.setFontArimoBold(context, pageTile, false);
        appName.setFocusable(true);

        if (pageModel.getPage_type_id() == 1 || pageModel.getPage_type_id() == 2) {
            appName.setFocusable(false);
        }
        if (GlobalFuncs.hasValue(graphics.getAppName())) {
            appName.setText(graphics.getAppName());
        }
        appName.requestFocus();

        if (pageModel.getMenu_title() != null) {
            pageTile.setText(pageModel.getMenu_title());
            separator.setText("|");
        }

        if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ) {
            appName.setTextColor(Color.parseColor(pageGraphics.getText_color()));
            pageTile.setTextColor(Color.parseColor(pageGraphics.getText_color()));
            separator.setTextColor(Color.parseColor(pageGraphics.getText_color()));
        }


        if (setSideImage) {
            ImageView sideImage = view.findViewById(R.id.sideImage);
            if (sideImage != null) {
                GlobalFuncs.setAppLogo(menuBar.getMenu_side_image(), context, sideImage);
            }
        }
    }



    private static  final int ANIMATION_DURATION = 500;

    public static void animateShowView(View view)
    {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null);
    }

    public static void animateHideHide(View view)
    {
//        view.animate()
//                .alpha(0f)
//                .setDuration(ANIMATION_DURATION)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
//                    }
//                });
    }



    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if (hours > 0) {
            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);

        }
        String duration =  (twoDigitString(minutes) + ":" + twoDigitString(seconds)).trim();
        duration = duration.replaceAll("\\s+", "");
        return duration;
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }



//    public static PlayList query(String )


    public static PlayList getContainer(int pageID) {
        for (PlayList container : typeContainers) {
            if (Arrays.asList(container.getPage_ids()).contains(String.valueOf(pageID))) {
                return container;
            }
        }
        return null;
    }

    public static List<PlayList> getContainerCategories(PlayList container) {
        if (container == null ) return null;
        return  (Stream.of(GlobalVars.typeCategories).filter(category -> Arrays.asList(category.getParent_category_ids()).contains(container.getEntity_id()))).collect(Collectors.toList());
    }


    public static List<PlayList> getCategoryContents(List<PlayList> categories) {
        List<PlayList> categoryContents = new ArrayList<>();
        if (categories ==null || categories.size() <= 0) {
            return  categoryContents;
        }

        for (PlayList category : categories) {
            for (PlayList content : typeContents) {
                if (Arrays.asList(content.getParent_category_ids()).contains(category.getEntity_id())) {
                    categoryContents.add(content);
                }
            }
        }

        return categoryContents;
    }


    public static Map<PlayList, ArrayList<VideoCard>> getContentVideos(List<PlayList> contents) {
        Map<PlayList, ArrayList<VideoCard>> contentVideos = new LinkedHashMap<>();
        if (contents ==null || contents.size() <= 0) {
            return  contentVideos;
        }

        for (PlayList content : contents) {
            for (VideoCard videoCard : allVideos) {
                if (content.getItemIds() != null && content.getItemIds().length > 0 && Arrays.asList(content.getItemIds()).contains(videoCard.getId())) {
                    if (contentVideos.get(content) == null) {
                        contentVideos.put(content, new ArrayList<>());
                    }
                    Objects.requireNonNull(contentVideos.get(content)).add(videoCard);
                }
            }
        }
        return contentVideos;
    }


    public static String setMacros( String url, VideoCard videoCard, PageModel pageModel, String eventID) {
        String errorMsg = "Macro Error";
        String TIMESTAMP = null;

        try {
            SimpleDateFormat formatter =   new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            TIMESTAMP = formatter.format(date);

        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}
        String REQUEST_ID = String.valueOf((int)(Math.random() * (100000000 - 1000000 + 1) + 1000000)); // get random request id
        String CACHEBUSTER = String.valueOf((int)(Math.random() * (100000000 - 1000000 + 1) + 1000000)); // get random category


        url = url.replace("[BRAND_NAME]",graphics == null ? "-1" : checkMacro(graphics.getBrandName()));
        url = url.replace("[APP_NAME]",graphics == null ? "-1" : checkMacro(graphics.getAppName()));
        url = url.replace("[CHANNEL_HASH]",info == null ? "-1" : checkMacro(info.getHash()));
        url = url.replace("[PUBLISHER_ID]",info == null ? "-1" : checkMacro(info.getPub_id()));
        url = url.replace("[[APP_BUNDLE]]",info == null ? "-1" : checkMacro(info.getApp_bundle()));
        url = url.replace("[APP_ID]",info == null ? "-1" : checkMacro(info.getApp_id()));
        url = url.replace("[VAST_ID]",adModel == null ? "-1" : checkMacro(adModel.getVastID()));

        url = url.replace("[EXTERNAL_IP]", checkMacro(USER_IP));
        url = url.replace("[REQ_IP]", checkMacro(USER_IP));
        url = url.replace("[[APP_VERSION]]",checkMacro(APP_VERSION));
        url = url.replace("[PLATFORM]","Android");
        url = url.replace("[REQUEST_ID]",checkMacro(REQUEST_ID));
        url = url.replace("[DEVICE_INFO]",checkMacro(DEVICE_INFO));
        url = url.replace("[USER_AGENT]",checkMacro(USER_AGENT));
        url = url.replace("[[USER_ID]]",checkMacro(null));
        url = url.replace("[DEVICE_ID]",checkMacro(DEVICE_ID));
        url = url.replace("[IDFA]",checkMacro(IDFA));
        url = url.replace("[IFA]",checkMacro(IDFA));
        url = url.replace("[IFA_TYPE]",checkMacro(IFA_TYPE));
        url = url.replace("[TIMESTAMP]",checkMacro(TIMESTAMP));
        url = url.replace("[ADS_TRACKING]",checkMacro(String.valueOf(ADS_TRACKING)));
        url = url.replace("[COUNTRY]",checkMacro(COUNTRY));
        url = url.replace("[LANGUAGE]",checkMacro(LANGUAGE));
        url = url.replace("[WIDTH]",checkMacro(String.valueOf(WIDTH)));
        url = url.replace("[HEIGHT]",checkMacro(String.valueOf(HEIGHT)));
        url = url.replace("[MAC_ADDRESS]",checkMacro(MAC_ADDRESS));
        url = url.replace("[CACHEBUSTER]", checkMacro(CACHEBUSTER));
        url = url.replace("[PAGE_ID]", pageModel == null ? "-1" : checkMacro(String.valueOf(pageModel.getPage_type_id())));

        url = url.replace("[VIDEO_TITLE]", videoCard == null ? "-1" : checkMacro(videoCard.getTitle()));
        url = url.replace("[CATEGORY_ID]",videoCard == null ? "-1" : checkMacro(videoCard.getId()));
        url = url.replace("[PLACEMENT_ID]",videoCard == null ? "-1" : checkMacro(videoCard.getId()));
        url = url.replace("[CAROUSEL_ID]",videoCard == null ? "-1" : checkMacro(videoCard.getId()));
        url = url.replace("[VIDEO_CONTENT_ID]",videoCard == null ? "-1" : checkMacro(videoCard.getId()));
        url = url.replace("[VIDEO_TRG_SEC]",videoCard == null ? "-1" : checkMacro(String.valueOf(videoCard.getCurrentPlayPosition())));
        url = url.replace("[VIDEO_CONTENT_TRIGGER_SECONDS]",videoCard == null ? "-1" : checkMacro(String.valueOf(videoCard.getCurrentPlayPosition())));
        url = url.replace("[EVENT_ID]", checkMacro(eventID));

//        url = url.replace("[IAB_CATEGORIES]",videoCard == null ? "-1" : checkMacro(String.valueOf(videoCard.getCurrentPlayPosition())));

        return url;
    }

    static String checkMacro(String macro) {
        return macro != null && !macro.isEmpty() ? macro : "-1";
    }

    private static int[] getScreenResolution(Context context)
    {
        int width = -1, height = -1;
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        } catch ( Exception e) { Log.e("GlobalFunc", "Macro Error", e); }

        return new int[]{width, height};
    }

    public static void extractMacros(Context context) {
        String errorMsg = "Macro Error";

        try {
            USER_IP =  GlobalFuncs.getExternalIpAddress();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            WIDTH = String.valueOf(getScreenResolution(context)[0]);
            HEIGHT = String.valueOf(getScreenResolution(context)[1]);
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}


        try {
            LANGUAGE = Locale.getDefault().getDisplayLanguage();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}


        try {
            COUNTRY = context.getResources().getConfiguration().locale.getCountry();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            USER_AGENT=  Util.getUserAgent(context, context.getString(R.string.app_name));
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            DEVICE_INFO = android.os.Build.MODEL;
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            MAC_ADDRESS = info.getMacAddress();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {

            if (DEVICE_INFO.equals("AFTMM")) {
                ContentResolver cr = context.getContentResolver();
                ADS_TRACKING = String.valueOf(Settings.Secure.getInt(cr, "limit_ad_tracking") != 0);
                IDFA = Settings.Secure.getString(cr, "advertising_id");
                IFA_TYPE = "afai";
            } else  {
                if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(context)) {
                    AdvertisingIdInfo advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context).get();
                    IDFA = advertisingIdInfo.getId();
                    ADS_TRACKING = String.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled());
                    IFA_TYPE = "aaid";
                }
            }


        } catch ( Exception e) { Log.e("GlobalFunc", errorMsg, e); }


    }



    public static String getExternalIpAddress() {
        try {
            URL myIP = new URL("https://checkip.amazonaws.com");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(myIP.openStream()));
                return in.readLine();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e) {
            Log.e("IP error", "Cannot get IP Address", e);
        }

        return null;
    }




    public static void showTvDialog(Activity activity, String title, String description, int DIALOG_REQUEST_CODE) {

        Intent intent = new Intent(activity, TVDialogActivity.class);
        intent.putExtra(ARG_TITLE_RES, title);
        intent.putExtra(ARG_ICON_RES, android.R.drawable.ic_dialog_alert);
        intent.putExtra(ARG_NEGATIVE_RES, R.string.dialog_no);
        intent.putExtra(ARG_POSITIVE_RES, R.string.dialog_yes);
        intent.putExtra(ARG_DESC_RES, description);
        intent.putExtra(REQUEST_CODE, DIALOG_REQUEST_CODE);
        activity.startActivityForResult(intent, DIALOG_REQUEST_CODE);

    }


    public static String checkString( String string) {
        if (string != null && !string.isEmpty()) {
            return string;
        }

        return null;
    }




    public static void showStreamLiveErrorMsg(Context context){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.custom_tv_dialog);
        Button yesBtn = dialog.findViewById(R.id.btn_yes);
        yesBtn.requestFocus();
        yesBtn.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    public static void changeToTRL(Activity activity) {
        if (graphics != null && graphics.isRtl()) {
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public static PageModel getPage(int pageID) {

        for (PageModel pageModel : menus.keySet()) {
            if ( pageModel.getPage_id() == pageID) {
                return pageModel;
            }
        }
        return null;
    }



    public static boolean hasValue(String val) {
        return val != null && !val.isEmpty();
    }




    public static void getUserOption(Context context, String dialogTittleTxt, String dialogMsgTxt, String noBtnText, String yesBtnText, Runnable noBtnFunc, Runnable yesBtnFunc){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_yes_no);
        Button yesBtn = dialog.findViewById(R.id.dialogYes);
        Button noBtn = dialog.findViewById(R.id.dialogNo);
        LinearLayout titleHolder = dialog.findViewById(R.id.titleHolder);
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView dialogMsg = dialog.findViewById(R.id.dialogMsg);

        FontStyles.setFontArimoBold(context, dialogTitle, false);
        FontStyles.setFontArimoBold(context, dialogMsg, false);

        dialogTitle.setText(dialogTittleTxt);
        dialogMsg.setText(dialogMsgTxt);
        noBtn.setText(noBtnText);
        yesBtn.setText(yesBtnText);

        if (dialogTittleTxt == null) {
            titleHolder.setVisibility(View.GONE);
        }

        if (noBtnText == null) {
            noBtn.setVisibility(View.GONE);
        }

        if (yesBtnText == null) {
            yesBtn.setVisibility(View.GONE);
        }

        noBtn.requestFocus();
        noBtn.setOnClickListener(view -> {
            dialog.dismiss();
            if (noBtnFunc != null) {
                noBtnFunc.run();
            }
        });
        yesBtn.setOnClickListener(view -> {
            dialog.dismiss();
            if (yesBtnFunc != null) {
                yesBtnFunc.run();
            }
        });

        dialog.show();

    }



    public static boolean isPlayerDirectChannel() {
        return graphics != null && GlobalFuncs.hasValue(graphics.getHomeScreen()) && Integer.parseInt(graphics.getHomeScreen()) <= 0;
    }


    public VideoCard cleanVideoString(VideoCard videoCard) {
        try{
            if (videoCard.getDescription().contains("\\\'")) {
                videoCard.setDescription(videoCard.getDescription().replace("\\\'", "'"));
            }

            if (videoCard.getDescription().contains("\\'")) {
                videoCard.setDescription(videoCard.getDescription().replace("\\'", "'"));
            }

            if (videoCard.getDescription().contains("\\\"")) {
                videoCard.setDescription(videoCard.getDescription().replace("\\\"", "\""));
            }


            if (videoCard.getTitle().contains("\\\'")) {
                videoCard.setTitle(videoCard.getTitle().replace("\\\'", "'"));
            }

            if (videoCard.getTitle().contains("\\'")) {
                videoCard.setTitle(videoCard.getTitle().replace("\\'",  "'"));
            }

            if (videoCard.getTitle().contains("\\\"")) {
                videoCard.setTitle(videoCard.getTitle().replace("\\\"", "\""));
            }

        }catch (Exception e) {
            Log.e(TAG, "String Cleaning", e);
        }
        return videoCard;

    }

}


