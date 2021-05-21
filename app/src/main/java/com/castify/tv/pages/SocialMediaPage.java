package com.castify.tv.pages;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SocialMediaPage extends Fragment {

    LinearLayout socialMediaContacts;
    public TextView aboutDesc;


    ArrayList<MenuPageItem> menuPageItems = new ArrayList<>();
    PageGraphics pageGraphics ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View socialMediaPageView =  inflater.inflate(R.layout.menu_social_media, container, false);

        socialMediaContacts = socialMediaPageView.findViewById(R.id.socialMediaContacts);
        aboutDesc = socialMediaPageView.findViewById(R.id.aboutDesc);
        FontStyles.setFontArimoBold(requireContext(), aboutDesc, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        PageModel pageModel = GlobalFuncs.getPage(bundle.getInt(GlobalVars.currentPageID));

        if ( pageModel != null ) {
            menuPageItems = GlobalVars.menus.get(pageModel);
        }

        pageGraphics = pageModel.getGraphic();
        GlobalFuncs.    setUpPageTitle(socialMediaPageView, pageModel, requireContext(), true, pageGraphics);
        aboutDesc.setText(pageModel.getPage_descrtipion());
        if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ) {
            aboutDesc.setTextColor(Color.parseColor(pageGraphics.getTitle_color()));
        }

        //Get the SocialMediaPage Media Items
        if (menuPageItems != null && menuPageItems.size() > 0) {
            for (MenuPageItem menuPageItem : menuPageItems) {
                try {
                    View socialMediaItem  = LayoutInflater.from(requireContext()).inflate(R.layout.menu_social_media_contact, null);
                    ImageView socialMediaLogo = socialMediaItem.findViewById(R.id.socialMediaLogo);
                    TextView socialMediaName = socialMediaItem.findViewById(R.id.socialMediaName);
                    socialMediaName.setText(menuPageItem.getItem_sub_type_id_name());
                    TextView socialMediaURL = socialMediaItem.findViewById(R.id.socialMediaURL);
                    ImageView socialMedaiQR = socialMediaItem.findViewById(R.id.socialMedaiQR);
                    FontStyles.setFontArimoBold(requireContext(), socialMediaName, false);
                    FontStyles.setFontArimoBold(requireContext(), socialMediaURL, false);

                    socialMediaURL.setText(menuPageItem.getItem_description());
                    GlobalFuncs.loadImage(menuPageItem.getItem_image(), requireContext(), socialMediaLogo);
                    GlobalFuncs.loadImage(menuPageItem.getQr_img(), requireContext(), socialMedaiQR);


                    if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()) ) {
                        socialMediaName.setTextColor(Color.parseColor(pageGraphics.getText_color()));
                        socialMediaURL.setTextColor(Color.parseColor(pageGraphics.getText_color()));
                    }

                    socialMediaContacts.addView(socialMediaItem);
//                    break;
                }catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "SocialMediaPage media extraction Error", e);
                }
            }
        }
        //Set side image
        return socialMediaPageView;
    }
}
