package com.example.admin.tripapplication.BLU;

import java.io.Serializable;

public class MyBleList implements Serializable {

    String userid;
    String id;
    String item;
    String mac;
    String icon;

    public MyBleList(){

    }

    public MyBleList(String userid, String id, String item, String mac, String icon) {
        this.userid = userid;
        this.id = id;
        this.item = item;
        this.mac = mac;
        this.icon = icon;
    }

    public String getUserid() { return userid; }

    public void setUserid(String userid) { this.userid = userid; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getItem() { return item; }

    public void setItem(String item) { this.item = item; }

    public String getMac() { return mac; }

    public void setMac(String mac) { this.mac = mac; }

    public String getIcon() { return icon; }

    public void setIcon(String icon) { this.icon = icon; }
}
