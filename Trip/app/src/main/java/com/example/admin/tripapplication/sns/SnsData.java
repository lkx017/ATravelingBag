package com.example.admin.tripapplication.sns;

import java.io.Serializable;

/**
 * Created by admin on 2018-05-30.
 */

public class SnsData implements Serializable {
    String id;
    String date;
    String title;
    String text;
    String imageurl;


    public SnsData() {
    }

    public SnsData(String id, String date, String title, String text, String imageurl) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.imageurl = imageurl;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
