package com.castify.tv.adapters;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.castify.tv.R;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.MenuPageItem;
import com.castify.tv.utils.GlobalFuncs;

import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.MyView> {
    private final ArrayList<MenuPageItem> menuPageItems;
    Context aContext;

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public StoreListAdapter(ArrayList<MenuPageItem> menuPageItems, Context aContext) {
        this.menuPageItems = menuPageItems;
        this.aContext = aContext;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.store_item,
                        parent,
                        false);

        return new MyView(itemView);
    }


    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        try {
            MenuPageItem menuPageItem = menuPageItems.get(position);
            GlobalFuncs.loadImage(menuPageItem.getItem_image(), aContext, holder.itemImage);
            Glide.with(aContext).asBitmap().load(menuPageItem.getQr_img()).into(holder.qrImage);

            holder.itemDescription.setText(menuPageItem.getItem_title());
            holder.itemLink.setText(menuPageItem.getShort_link_url().replace("https://", ""));


            String salePrice = GlobalFuncs.checkString(menuPageItem.getItem_sale_price() );
            String retailPrice = GlobalFuncs.checkString(menuPageItem.getItem_retail_price() );
            if (salePrice != null) {
                holder.itemSalePrice.setVisibility(View.VISIBLE);
                holder.itemRetailPrice.setText(salePrice, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) holder.itemRetailPrice.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, menuPageItem.getItem_retail_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            
            if (retailPrice != null) {
                holder.itemRetailPrice.setVisibility(View.VISIBLE);
                holder.itemRetailPrice.setText(retailPrice);
            }

        } catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error", e);

        }
    }

    @Override
    public int getItemCount()
    {
        return menuPageItems.size();
    }


    public class MyView extends RecyclerView.ViewHolder {

        TextView itemDescription;
        TextView itemSalePrice;
        TextView itemRetailPrice;
        TextView quickBuy;
        TextView itemCurrency;
        TextView itemLink;

        ImageView itemImage;
        ImageView qrImage;

        LinearLayout innerView;

        public MyView(View view) {
            super(view);
            itemDescription = view.findViewById(R.id.itemDescription);
            itemSalePrice = view.findViewById(R.id.itemSalePrice);
            itemRetailPrice = view.findViewById(R.id.itemRetailPrice);
            quickBuy = view.findViewById(R.id.quickBuy);
            itemLink = view.findViewById(R.id.itemLink);
            itemCurrency = view.findViewById(R.id.itemCurrency);
            innerView = view.findViewById(R.id.innerView);

            itemImage = view.findViewById(R.id.itemImage);
            qrImage = view.findViewById(R.id.qrImage);

            itemImage.setClipToOutline(true);


            FontStyles.setFontArimoBold(aContext, itemDescription, false);
            FontStyles.setFontArimoBold(aContext, itemSalePrice, false);
            FontStyles.setFontArimoBold(aContext, itemRetailPrice, false);
            FontStyles.setFontArimoBold(aContext, quickBuy, false);
            FontStyles.setFontArimoBold(aContext, itemCurrency, false);
            FontStyles.setFontArimoBold(aContext, itemLink, false);

            String currency = "$";
            if (GlobalVars.graphics.isRtl()) {
                currency = "₪";
            }
            itemCurrency.setText(currency);

            innerView.setOnFocusChangeListener((view1, hasFocus) -> {
                if (hasFocus) {
                    innerView.setBackground(ContextCompat.getDrawable(aContext, R.drawable.store_border));
                    return;
                }
                innerView.setBackgroundColor(Color.WHITE);

            });
        }
    }
}
