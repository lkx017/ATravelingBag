package com.example.admin.tripapplication.BLU;


public class SensorList {
    String name;
    String MAC;
    int rssi;

    public SensorList(String name, String MAC, int rssi) {
        this.name = name;
        this.MAC = MAC;
        this.rssi=rssi;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMAC() { return MAC; }

    public void setMAC(String MAC) { this.MAC = MAC; }

    public int getRssi() { return rssi; }

    public void setRssi(int rssi) { this.rssi = rssi; }


}
