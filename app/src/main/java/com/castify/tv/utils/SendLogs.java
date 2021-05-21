package com.castify.tv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.castify.tv.models.PageModel;
import com.castify.tv.models.VideoCard;

public class SendLogs extends Thread {

    public static String TAG = "Beacon";

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final String eventID;
    private final VideoCard videoCard;
    private final PageModel pageModel;

     public SendLogs(Context context, String eventID, VideoCard videoCard, PageModel pageModel) {
         this.context = context;
         this.eventID = eventID;
         this.videoCard = videoCard;
         this.pageModel = pageModel;

     }


    @Override
    public void run() {
        try {

            String beaconURL = GlobalFuncs.setMacros(GlobalVars.BEACON_url, videoCard, pageModel, eventID);

            //Format Beacon URL
            HttpConnect.sendLogs(context, beaconURL);

        } catch (Exception e) {
            Log.e(TAG, "BEACON", e);
        }
    }
}
