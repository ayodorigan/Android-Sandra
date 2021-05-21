package com.castify.tv.presenters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;

import com.castify.tv.R;
import com.castify.tv.models.PageGraphics;
import com.castify.tv.utils.FontStyles;
import com.castify.tv.utils.GlobalFuncs;
import com.castify.tv.utils.GlobalVars;


public class CustomRowHeaderPresenter extends RowHeaderPresenter {
    PageGraphics pageGraphics;
    int paddingStart;
    public CustomRowHeaderPresenter(PageGraphics pageGraphics, int paddingStart) {
        this.pageGraphics = pageGraphics;
        this.paddingStart = paddingStart;
    }
    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return super.onCreateViewHolder(parent);

    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        HeaderItem headerItem = item == null ? null : ((Row) item).getHeaderItem();
        RowHeaderPresenter.ViewHolder vh = (RowHeaderPresenter.ViewHolder) viewHolder;
        vh.view.setAlpha((float) 1.0);
        TextView title = vh.view.findViewById(R.id.row_header);
        title.setPadding(paddingStart, 0, 0, 0);
        assert headerItem != null;
        if(!TextUtils.isEmpty(headerItem.getName())) {
            title.setText(headerItem.getName().trim());
            title.setAlpha((float) 1.0);
        }
        FontStyles.setFontArimoBold(title.getContext(), title, true);

        if (pageGraphics != null && GlobalFuncs.hasValue(pageGraphics.getText_color())) {
            title.setTextColor(Color.parseColor(pageGraphics.getTitle_color()));
        }
    }
}
