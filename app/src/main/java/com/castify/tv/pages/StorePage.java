package com.castify.tv.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.castify.tv.adapters.StoreListAdapter;
import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.MenuPageItem;

import java.util.ArrayList;
import java.util.Objects;

public class StorePage extends Fragment {

    public TextView  genDescription, pageDesc;

    ImageView qenQrImage ;

    RecyclerView storeListing;
    StoreListAdapter storeListAdapter;
    PageModel pageModel;
    PageGraphics pageGraphics ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchView =  inflater.inflate(R.layout.menu_store, container, false);

        storeListing = searchView.findViewById(R.id.storeListing);
        genDescription = searchView.findViewById(R.id.genDescription);
        pageDesc = searchView.findViewById(R.id.pageDesc);
        qenQrImage = searchView.findViewById(R.id.qenQrImage);
        FontStyles.setFontArimoBold(requireContext(), genDescription, false);
        FontStyles.setFontArimoBold(requireContext(), pageDesc, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        pageModel = GlobalFuncs.getPage(bundle.getInt(GlobalVars.currentPageID));
        pageGraphics = pageModel.getGraphic();

        ArrayList<MenuPageItem> menuPageItems = GlobalVars.menus.get(pageModel);
        assert pageModel != null;
        GlobalFuncs.setUpPageTitle(searchView, pageModel, requireContext(), false, pageGraphics);

        genDescription.setText(pageModel.getPage_footer_text());
        pageDesc.setText(pageModel.getPage_descrtipion());
        Glide.with(this).asBitmap().load(pageModel.getPage_footer_action_link_qr()).into(qenQrImage);

        storeListAdapter = new StoreListAdapter(menuPageItems, requireContext());

        GridLayoutManager linearLayoutManager = new GridLayoutManager(requireActivity(), 4);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        storeListing.setLayoutManager(linearLayoutManager);
        storeListing.setAdapter(storeListAdapter);
        storeListing.requestFocus(); // Request focus to this recycler view as soon as the page is opened.

        return searchView;

    }


}
