package com.castify.tv.pages;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.models.PageModel;
import com.castify.tv.utils.GlobalVars;

public class AboutPage extends Fragment {

    // Textviews
    public TextView aboutDesc;
    PageGraphics pageGraphics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.menu_about, container, false);

        // Initialize
        aboutDesc = view.findViewById(R.id.aboutDesc);

        //Get PageModel
        Bundle bundle = getArguments();
        assert bundle != null;
        int pageID = bundle.getInt(GlobalVars.currentPageID);
        PageModel pageModel = GlobalFuncs.getPage(pageID);
        if ( pageModel == null) {return view;}

        pageGraphics = pageModel.getGraphic();
        GlobalFuncs.setUpPageTitle(view, pageModel, requireContext(), false, pageGraphics);
        aboutDesc.setText(pageModel.getPage_descrtipion());

        if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ) {
            aboutDesc.setTextColor(Color.parseColor(pageGraphics.getTitle_color()));
        }

        FontStyles.setFontArimoBold(requireContext(), aboutDesc, false);

        return view;
    }


    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }
}
