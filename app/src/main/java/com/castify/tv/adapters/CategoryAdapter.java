package com.castify.tv.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.castify.tv.R;
import com.castify.tv.activities.MainActivity;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;
import com.castify.tv.models.PlayList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends HorizontalGridView.Adapter<CategoryAdapter.MyView>  {
    private final List<PlayList> categories;
    Context aContext;
    String categoryType;
    FrameLayout catVideos;
    int selected_category=0;
    PageGraphics pageGraphics;
    MainActivity mainActivity;

    private final OnCatListener onCatListener;
    public CategoryAdapter(Context aContext, List<PlayList> categories, String categoryType, OnCatListener onCatListener, FrameLayout catVideos, PageGraphics pageGraphics, MainActivity mainActivity) {
        this.categories = categories;
        this.aContext = aContext;
        this.categoryType = categoryType;
        this.onCatListener =  onCatListener;
        this.catVideos = catVideos;
        this.pageGraphics = pageGraphics;
        this.mainActivity = mainActivity;
    }



    @NotNull
    @Override
    public MyView onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        int catLayout = -1;
        if (categoryType.equals(GlobalVars.categoryTypeStory)) {
            catLayout = R.layout.category_story;

        } else if (categoryType.equals(GlobalVars.categoryTypeParalaxBox)) {
            catLayout = R.layout.category_basic;
        }

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(catLayout,
                        parent,
                        false);


        // return itemView
        return new MyView(itemView, onCatListener, pageGraphics);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        PlayList category = categories.get(position);
        holder.categoryTitle.setText(category.getName());
        GlobalFuncs.loadImage(category.getImage(), aContext, holder.categoryThumbnail);
        if (categoryType.equals(GlobalVars.categoryTypeStory)) {
            FontStyles.setFontArimoBold(aContext, holder.categoryTitle, selected_category == position);
            holder.categoryTitle.setText(category.getName());
            catVideos.requestFocus();
        }
        if (categoryType.equals(GlobalVars.categoryTypeParalaxBox)) {
            if (selected_category == position) {
                holder.categoryTitle.setBackgroundColor(Color.WHITE);
                holder.categoryTitle.setTextColor(Color.BLACK);;
            } else {
                holder.categoryTitle.setBackgroundColor(aContext.getResources().getColor(android.R.color.holo_red_dark));
                holder.categoryTitle.setTextColor(Color.WHITE);
            }
            catVideos.requestFocus();
        }
    }

    @Override
    public int getItemCount()
    {
        return categories.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("junnnjnfr", "Riuhfnffn");
                return false;
            }

        });
    }

    public class MyView extends RecyclerView.ViewHolder implements  View.OnClickListener , View.OnFocusChangeListener{

        // Text View
        TextView categoryTitle;
        ImageView categoryThumbnail;
        OnCatListener onCatListener;
        boolean focusOnMenu;

        public MyView(View view, OnCatListener onCatListener, PageGraphics pageGraphics) {
            super(view);
            categoryTitle = view.findViewById(R.id.categoryTitle);
            categoryThumbnail = view.findViewById(R.id.categoryThumbnail);
            FontStyles.setFontArimoBold(aContext, categoryTitle, false);
            view.setOnClickListener(this);
            this.onCatListener = onCatListener;
            categoryTitle.setTextColor(Color.parseColor(GlobalVars.graphics.getTextColor()));
            if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color()))  {
                categoryTitle.setTextColor(Color.parseColor(pageGraphics.getText_color()));
            }
            view.setOnFocusChangeListener((view1, hasFocus) -> {
                int[] dimen = getCatFocusedItemDimension(hasFocus);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dimen[0], dimen[1]);
                if (categoryType.equals(GlobalVars.categoryTypeStory)) {
                    categoryThumbnail.setLayoutParams(layoutParams);
                    final ChangeBounds transition = new ChangeBounds();
                    transition.setDuration(50L);
                    TransitionManager.beginDelayedTransition((LinearLayout)view, transition);

                }

//               if (hasFocus){
//                   // Highlight the selected parallax box
//                   if (categoryType.equals(GlobalVars.categoryTypeParalaxBox)) {
//                       categoryTitle.setBackgroundColor(Color.WHITE);
//                       categoryTitle.setTextColor(Color.BLACK);
//
//                   }
//               } else {
//                   if (categoryType.equals(GlobalVars.categoryTypeParalaxBox)) {
//                       categoryTitle.setBackgroundColor(aContext.getResources().getColor(android.R.color.holo_red_dark));
//                       categoryTitle.setTextColor(Color.WHITE);
//
//                   }
//               }
            });

            view.setOnKeyListener((view1, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_DPAD_LEFT && getAdapterPosition() == 0) {
                    if (focusOnMenu) {
                        focusOnMenu = false;
                        mainActivity.menuHomeButton.requestFocus(); // Focus om the videos container
                        return true;
                    }
                    focusOnMenu = true;
                }
                return false;
            });

        }


        @Override
        public void onClick(View view) {
            onCatListener.OnCatClickListener(getAdapterPosition(), view, view.isFocused());
            selected_category=getAdapterPosition();
            notifyDataSetChanged();

        }

        @Override
        public void onFocusChange(View view, boolean b) {
            Log.e("focusfocus", b + "");
        }


    }

    public interface OnCatListener {
        void OnCatClickListener(int pos, View view, boolean isSelected);
    }

    protected int[] getCatFocusedItemDimension(boolean hasFocus) {
        switch (categoryType) {
            case "story":
                return  hasFocus ?
                        new int[]{aContext.getResources().getDimensionPixelOffset(R.dimen.cat_story_selected_width),
                        aContext.getResources().getDimensionPixelOffset(R.dimen.cat_story_selected_height)} :
                        new int[]{aContext.getResources().getDimensionPixelOffset(R.dimen.cat_story_unselected_width),
                                aContext.getResources().getDimensionPixelOffset(R.dimen.cat_story_unselected_height)};
            case "paralaxBox":
                return  hasFocus ?
                        new int[]{aContext.getResources().getDimensionPixelOffset(R.dimen.cat_paralaxbox_selected_width),
                                aContext.getResources().getDimensionPixelOffset(R.dimen.cat_paralaxbox_selected_height)} :
                        new int[]{aContext.getResources().getDimensionPixelOffset(R.dimen.cat_paralaxbox_unselected_width),
                                aContext.getResources().getDimensionPixelOffset(R.dimen.cat_paralaxbox_unselected_height)};

        }
        return new int[]{0,0};
    }
}
