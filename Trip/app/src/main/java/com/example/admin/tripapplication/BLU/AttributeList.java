package com.example.admin.tripapplication.BLU;

import java.io.Serializable;

public class AttributeList implements Serializable {
    String id;
    String item;
    String MAC;
    int icon;
   // BluetoothDevice devicesDiscovered;

    public AttributeList(String id, String item, String MAC, int icon) {
        this.id = id;
        this.item = item;
        this.MAC = MAC;
        this.icon = icon;
       // this.devicesDiscovered = devicesDiscovered;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getItem() { return item; }

    public void setItem(String item) { this.item = item; }

    public String getMAC() { return MAC; }

    public void setMAC(String MAC) { this.MAC = MAC; }

    public int getIcon() { return icon; }

    public void setIcon(int icon) { this.icon = icon; }
}
