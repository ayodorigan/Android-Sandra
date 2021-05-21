package com.castify.tv.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class MenuBar {
    @SerializedName("test")

    private String menu_template_id;

    private String menu_icon;

    private String menu_logo_image;

    private String menu_side_image;

    private String menu_background_color;

    private String menu_text_color = "#444444";

    private String menu_text_color_hover = "#FFFFFF";

    private String menu_text_color_selected = "#FFFFFF";

    private String menu_open;

    private String menu_side_position;

    private String favorites_menu;

    private String continue_watching_menu;

    private JSONObject[] pages;


//    private String  pages;

    public String getMenu_template_id() {
        return menu_template_id;
    }

    public void setMenu_template_id(String menu_template_id) {
        this.menu_template_id = menu_template_id;
    }

    public String getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(String menu_icon) {
        this.menu_icon = menu_icon;
    }

    public String getMenu_logo_image() {
        return menu_logo_image;
    }

    public void setMenu_logo_image(String menu_logo_image) {
        this.menu_logo_image = menu_logo_image;
    }

    public String getMenu_side_image() {
        return menu_side_image;
    }

    public void setMenu_side_image(String menu_side_image) {
        this.menu_side_image = menu_side_image;
    }

    public String getMenu_background_color() {
        return menu_background_color;
    }

    public void setMenu_background_color(String menu_background_color) {
        this.menu_background_color = menu_background_color;
    }

    public String getMenu_text_color() {
        return menu_text_color;
    }

    public void setMenu_text_color(String menu_text_color) {
        this.menu_text_color = menu_text_color;
    }

    public String getMenu_text_color_hover() {
        return menu_text_color_hover;
    }

    public void setMenu_text_color_hover(String menu_text_color_hover) {
        this.menu_text_color_hover = menu_text_color_hover;
    }

    public String getMenu_text_color_selected() {
        return menu_text_color_selected;
    }

    public void setMenu_text_color_selected(String menu_text_color_selected) {
        this.menu_text_color_selected = menu_text_color_selected;
    }

    public String getMenu_open() {
        return menu_open;
    }

    public void setMenu_open(String menu_open) {
        this.menu_open = menu_open;
    }

    public String getMenu_side_position() {
        return menu_side_position;
    }

    public void setMenu_side_position(String menu_side_position) {
        this.menu_side_position = menu_side_position;
    }

    public String getFavorites_menu() {
        return favorites_menu;
    }

    public void setFavorites_menu(String favorites_menu) {
        this.favorites_menu = favorites_menu;
    }

    public String getContinue_watching_menu() {
        return continue_watching_menu;
    }

    public void setContinue_watching_menu(String continue_watching_menu) {
        this.continue_watching_menu = continue_watching_menu;
    }


    //    public String getPages() {
//        return pages;
//    }
//
//    public void setPages(String pages) {
//        this.pages = pages;
//    }
}
