package com.castify.tv.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.ads.identifier.AdvertisingIdClient;
import androidx.ads.identifier.AdvertisingIdInfo;

import com.google.android.exoplayer2.util.Util;
import com.castify.tv.R;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.VideoCard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetMacros  extends AsyncTask<Object, Void, String>{
    @Override
    protected String doInBackground(Object... params) {
        Context context = (Context) params[0];
        String url = (String)params[1];
        VideoCard videoCard = (VideoCard)params[2];
        PageModel pageModel = (PageModel)params[3];

        String errorMsg = "Macro Error";
        int  REQUEST_ID = -1;
        String DEVICE_INFO = null;
        String USER_AGENT = null;
        String DEVICE_ID = null;
        AdvertisingIdInfo advertisingIdInfo = null;
        String COUNTRY = null;
        String LANGUAGE = null;
        String MAC_ADDRESS = null;
        String TIMESTAMP = null;

        try {
            LANGUAGE = Locale.getDefault().getDisplayLanguage();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            TIMESTAMP = formatter.format(date);

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
            REQUEST_ID = (int)(Math.random() * (100000000 - 1000000 + 1) + 1000000); // get random category
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            MAC_ADDRESS = info.getMacAddress();
        }catch (Exception e) { Log.e("GlobalFunc", errorMsg , e);}

        try {
            if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(context)) {
                advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context).get();
            }

        } catch ( Exception e) { Log.e("GlobalFunc", errorMsg, e); }



        url = url.replace("[EXTERNAL_IP]", checkMacro(GlobalFuncs.getExternalIpAddress()));
        url = url.replace("[BRAND_NAME]",checkMacro(GlobalVars.graphics.getBrandName()));
        url = url.replace("[APP_NAME]",checkMacro(GlobalVars.graphics.getAppName()));
        url = url.replace("[CHANNEL_HASH]",checkMacro(GlobalVars.info.getHash()));
        url = url.replace("[PLATFORM]","Android");
        url = url.replace("[[APP_VERSION]]",checkMacro(GlobalVars.APP_VERSION));
        url = url.replace("[PUBLISHER_ID]",checkMacro(GlobalVars.info.getPub_id()));
        url = url.replace("[[APP_BUNDLE]]",checkMacro(GlobalVars.info.getApp_bundle()));
        url = url.replace("[APP_ID]",checkMacro(GlobalVars.info.getApp_id()));
        url = url.replace("[REQUEST_ID]",checkMacro(String.valueOf(REQUEST_ID)));
        url = url.replace("[DEVICE_INFO]",checkMacro(DEVICE_INFO));
        url = url.replace("[USER_AGENT]",checkMacro(USER_AGENT));
        url = url.replace("[VIDEO_CONTENT_ID]",checkMacro(videoCard.getId()));
        url = url.replace("[[VIDEO_TRG_SEC]]",checkMacro(String.valueOf(videoCard.getCurrentPlayPosition())));
        url = url.replace("[[USER_ID]]",checkMacro(null));
        url = url.replace("[DEVICE_ID]",checkMacro(DEVICE_ID));
        url = url.replace("[IDFA]",checkMacro(advertisingIdInfo.getId()));
        url = url.replace("[TIMESTAMP]",checkMacro(TIMESTAMP));
        url = url.replace("[ADS_TRACKING]",checkMacro(String.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled())));
        url = url.replace("[COUNTRY]",checkMacro(COUNTRY));
        url = url.replace("[LANGUAGE]",checkMacro(LANGUAGE));
        url = url.replace("[VIDEO_TITLE]",videoCard.getTitle());
        url = url.replace("[WIDTH]",checkMacro(String.valueOf(getScreenResolution(context)[0])));
        url = url.replace("[HEIGHT]",checkMacro(String.valueOf(getScreenResolution(context)[1])));
        url = url.replace("[CATEGORY_ID]",checkMacro(videoCard.getId()));
        url = url.replace("[PLACEMENT_ID]",checkMacro(videoCard.getId()));
        url = url.replace("[VAST_ID]",checkMacro(GlobalVars.adModel.getVastID()));
        url = url.replace("[PAGE_ID]",checkMacro(String.valueOf(pageModel.getPage_type_id())));
        url = url.replace("[CAROUSEL_ID]",checkMacro(videoCard.getId()));
        url = url.replace("[MAC_ADDRESS]",checkMacro(MAC_ADDRESS));

        return url;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public
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
}
