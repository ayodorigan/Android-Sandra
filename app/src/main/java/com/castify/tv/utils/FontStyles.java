package com.castify.tv.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;


public class FontStyles {

    public static void setFontArimoBold (Context context, TextView textView, boolean isBold) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/arimo_regular.ttf");
        if (isBold) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/arimo_bold.ttf");
        }
        textView.setTypeface(typeface);
    }
}
