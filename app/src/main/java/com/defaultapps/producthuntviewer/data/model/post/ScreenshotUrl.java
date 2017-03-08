package com.defaultapps.producthuntviewer.data.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScreenshotUrl {

    @SerializedName("300px")
    @Expose
    private String img300px;
    @SerializedName("850px")
    @Expose
    private String img850px;

    public String get300px() {
        return img300px;
    }

    public void set300px(String img300px) {
        this.img300px = img300px;
    }

    public String get850px() {
        return img850px;
    }

    public void set850px(String img850px) {
        this.img850px = img850px;
    }

}