package com.castify.tv.dialogs;

import android.content.Context;

import com.castify.tv.R;
import com.castify.tv.activities.MainActivity;
import com.castify.tv.models.ErrorModel;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.utils.SendLogs;

public class AlertDialogs {

    public static void appExit(Context context){
        String msg = "Are you sure you want to exit?";
        String noBtnText = "No";
        String yesBtnText = "Yes";
        Runnable yesFunc = () -> {
            new SendLogs(context, GlobalVars.EXIT_APP, null, null).start();
            try {
                Thread.sleep(500);
            }catch (Exception ignored) {}
            System.exit(1);
        };

        GlobalFuncs.getUserOption(
                context,
                null,
                msg, noBtnText, yesBtnText
                , null,
                yesFunc);
    }

}
