package com.castify.tv.pages;

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
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.MenuPageItem;

import java.util.ArrayList;
import java.util.Objects;

public class ContactPage extends Fragment {

    private ArrayList<MenuPageItem> pageItems = new ArrayList<>();

    private MenuPageItem contactMenuItem;
    PageGraphics pageGraphics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.menu_contact, container, false);

        TextView contactDescription = view.findViewById(R.id.contactDescription);
        TextView contact = view.findViewById(R.id.contact);
        FontStyles.setFontArimoBold(requireContext(), contact, false);
        FontStyles.setFontArimoBold(requireContext(), contactDescription, false);


        Bundle bundle = getArguments();
        assert bundle != null;
        PageModel pageModel = bundle.getParcelable(GlobalVars.currentPageModelTag);
        GlobalFuncs.setUpPageTitle(view, pageModel, requireContext(), true, pageGraphics);

        pageItems = GlobalVars.menus.get(pageModel);

        if ( pageItems != null && pageItems.size() > 0) {
            contactMenuItem = pageItems.get(0);
        }


        //Set side image

        contactDescription.setText(pageModel.getPage_descrtipion());
        contact.setText(contactMenuItem.getItem_description());
        return view;
    }
}
