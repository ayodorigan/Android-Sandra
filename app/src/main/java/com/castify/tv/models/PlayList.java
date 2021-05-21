package com.castify.tv.models;

import java.io.Serializable;

public class PlayList implements Serializable {
    private String entity_id;

    private String type;

    private String name;

    private String[] page_type_ids;

    private String description;

    private String[] parent_category_ids;

    private boolean detailedCarousel;

    private String keyword;

    private String color;

    private String text_color;

    private String background_image;

    private String[] itemIds;

    private String[] page_ids;

    private int level_id;

    private String image;

    public String[] getPage_type_ids() {
        return page_type_ids;
    }

    public void setPage_type_ids(String[] page_type_ids) {
        this.page_type_ids = page_type_ids;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getParent_category_ids() {
        return parent_category_ids;
    }

    public void setParent_category_ids(String[] parent_category_ids) {
        this.parent_category_ids = parent_category_ids;
    }

    public String[] getItemIds() {
        return itemIds;
    }

    public void setItemIds(String[] itemIds) {
        this.itemIds = itemIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String[] getPage_ids() {
        return page_ids;
    }

    public void setPage_ids(String[] page_ids) {
        this.page_ids = page_ids;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDetailedCarousel() {
        return detailedCarousel;
    }

    public void setDetailedCarousel(boolean detailedCarousel) {
        this.detailedCarousel = detailedCarousel;
    }
}
