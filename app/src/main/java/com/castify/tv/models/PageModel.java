package com.castify.tv.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class PageModel implements Parcelable {
    private int page_id;
    private String page_menu_icon;
    private String menu_title;
    private int page_type_id;
    private String page_title;
    private String page_descrtipion;
    private String general_link;
    private String page_footer_text;
    private String page_footer_action_title;
    private String page_footer_action_link_qr;
    private String page_background_image;

    private JSONObject[] items;
    private PageGraphics graphic;


    public static final Creator<PageModel> CREATOR = new Creator<PageModel>() {
        @Override
        public PageModel createFromParcel(Parcel in) {
            return new PageModel(in);
        }

        @Override
        public PageModel[] newArray(int size) {
            return new PageModel[size];
        }
    };

    public String getPage_menu_icon() {
        return page_menu_icon;
    }

    public void setPage_menu_icon(String page_menu_icon) {
        this.page_menu_icon = page_menu_icon;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public int getPage_type_id() {
        return page_type_id;
    }

    public void setPage_type_id(int page_type_id) {
        this.page_type_id = page_type_id;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public String getPage_descrtipion() {
        return page_descrtipion;
    }

    public void setPage_descrtipion(String page_descrtipion) {
        this.page_descrtipion = page_descrtipion;
    }

    public String getGeneral_link() {
        return general_link;
    }

    public void setGeneral_link(String general_link) {
        this.general_link = general_link;
    }

    public String getPage_footer_text() {
        return page_footer_text;
    }

    public void setPage_footer_text(String page_footer_text) {
        this.page_footer_text = page_footer_text;
    }

    public String getPage_footer_action_title() {
        return page_footer_action_title;
    }

    public void setPage_footer_action_title(String page_footer_action_title) {
        this.page_footer_action_title = page_footer_action_title;
    }

    public String getPage_footer_action_link_qr() {
        return page_footer_action_link_qr;
    }

    public void setPage_footer_action_link_qr(String page_footer_action_link_qr) {
        this.page_footer_action_link_qr = page_footer_action_link_qr;
    }

    public JSONObject[] getItems() {
        return items;
    }

    public void setItems(JSONObject[] items) {
        this.items = items;
    }

    public static Creator<PageModel> getCREATOR() {
        return CREATOR;
    }

    public String getPage_background_image() {
        return page_background_image;
    }


    public void setPage_background_image(String page_background_image) {
        this.page_background_image = page_background_image;
    }

    public PageGraphics getGraphic() {
        return graphic;
    }

    public void setGraphic(PageGraphics graphic) {
        this.graphic = graphic;
    }

    PageModel(Parcel in) {
        page_menu_icon = in.readString();
        menu_title = in.readString();
        page_type_id = in.readInt();
        page_title = in.readString();
        page_descrtipion = in.readString();
        page_id = in.readInt();
        page_footer_text = in.readString();
        page_footer_action_title = in.readString();
        page_footer_action_link_qr = in.readString();
        general_link = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeString(page_menu_icon);
         parcel.writeString(menu_title);
         parcel.writeInt(page_type_id);
         parcel.writeString(page_title);
         parcel.writeString(page_descrtipion);
         parcel.writeInt(page_id);
         parcel.writeString(page_footer_text);
         parcel.writeString(page_footer_action_title);
         parcel.writeString(page_footer_action_link_qr);
         parcel.writeString(general_link);
    }


}

