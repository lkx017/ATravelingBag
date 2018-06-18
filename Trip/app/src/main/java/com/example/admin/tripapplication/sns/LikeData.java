package com.example.admin.tripapplication.sns;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 2018-06-06.
 */

public class LikeData implements Serializable {
    String getID;
    String getTitle;
    String getDate;
    String setID;
    String setText;

    public LikeData() {
    }

    public LikeData(String getID, String getTitle, String getDate, String setID, String setText) {
        this.getID = getID;
        this.getTitle = getTitle;
        this.getDate = getDate;
        this.setID = setID;
        this.setText = setText;
    }

    public String getGetID() {
        return getID;
    }

    public void setGetID(String getID) {
        this.getID = getID;
    }

    public String getGetTitle() {
        return getTitle;
    }

    public void setGetTitle(String getTitle) {
        this.getTitle = getTitle;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getSetID() {
        return setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public String getSetText() {
        return setText;
    }

    public void setSetText(String setText) {
        this.setText = setText;
    }
}
