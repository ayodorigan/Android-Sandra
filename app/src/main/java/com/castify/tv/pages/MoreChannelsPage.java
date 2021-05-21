package com.castify.tv.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.models.PageModel;
import com.castify.tv.utils.GlobalVars;

public class MoreChannelsPage extends Fragment {

    PageGraphics pageGraphics ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.menu_about, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        PageModel pageModel = bundle.getParcelable(GlobalVars.currentPageModelTag);
        GlobalFuncs.setUpPageTitle(view, pageModel, requireContext(), true, pageGraphics);

        return view;
    }
}
