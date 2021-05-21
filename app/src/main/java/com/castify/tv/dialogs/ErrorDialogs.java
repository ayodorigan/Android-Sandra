package com.castify.tv.dialogs;

import android.content.Context;

import com.castify.tv.R;
import com.castify.tv.activities.PlayerActivity;
import com.castify.tv.models.ErrorModel;
import com.castify.tv.utils.GlobalFuncs.*;
import com.castify.tv.utils.GlobalVars;

import static com.castify.tv.utils.GlobalFuncs.*;

public class ErrorDialogs {


    /**
     * Errors
     */
    public static ErrorModel NO_INTERNET_ERROR = new ErrorModel("0012", R.string.no_internet);
    public static ErrorModel NO_SPLASH_SCREEN_ERROR = new ErrorModel("0012", R.string.videos_loading_error);
    public static ErrorModel NO_CONTENT_ERROR = new ErrorModel("0013", R.string.no_content);
    public static ErrorModel VIDEO_LOAD_ERROR = new ErrorModel("0011", R.string.no_splash_screen);
    public static ErrorModel INVALID_FEED_ERROR = new ErrorModel("0014", R.string.corrupt_json);
    public static ErrorModel VIDEO_STREAM_ERROR = new ErrorModel("0010", R.string.video_stream_error);
    public static ErrorModel GEN_ERROR = new ErrorModel("0020", R.string.error_fragment_message);



    public static void showVideStreamError(Context context, Runnable noBtnFunc,  Runnable yesBtnFun) {
        String noBtnText = isPlayerDirectChannel() ? "Exit" : "Go Back";
        String msg = context.getString(VIDEO_STREAM_ERROR.getErrorMsg());
        String yesBtnText = "Next Video";
        getUserOption(
                context,
                "Video Player",
                msg, noBtnText, yesBtnText
                , noBtnFunc,
                yesBtnFun);
    }

    public static void noInternetError(Context context, Runnable noBtnFunc, Runnable yesBtnFun) {
        String msg = context.getString(NO_INTERNET_ERROR.getErrorMsg());
        String yesBtnText = "OK";
        getUserOption(
                context,
                null,
                msg,
                null,
                yesBtnText,
                noBtnFunc,
                yesBtnFun);
    }


    public static void genError(Context context, Runnable noBtnFunc,  Runnable yesBtnFun) {
        String msg = context.getString(GEN_ERROR.getErrorMsg());
        String yesBtnText = "OK";
        getUserOption(
                context,
                null,
                msg,
                null,
                yesBtnText,
                noBtnFunc,
                yesBtnFun);
    }


    public static void noContentError(Context context, Runnable noBtnFunc, Runnable yesBtnFun) {
        String msg = context.getString(NO_CONTENT_ERROR.getErrorMsg());
        String yesBtnText = "OK";
        getUserOption(
                context,
                null,
                msg,
                null,
                yesBtnText,
                noBtnFunc,
                yesBtnFun);
    }


    public static void invalidFeedError(Context context,  Runnable noBtnFunc, Runnable yesBtnFun) {
        String msg = context.getString(INVALID_FEED_ERROR.getErrorMsg());
        String yesBtnText = "OK";
        getUserOption(
                context,
                null,
                msg,
                null,
                yesBtnText,
                noBtnFunc,
                yesBtnFun);
    }
}
