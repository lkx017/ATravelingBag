package com.example.admin.tripapplication.BLU;

import java.io.Serializable;

public class MyList implements Serializable {
    String name;
    String item;
    String MAC;
    int icon;
    String con_state;
    int signal_state;
    int siran;
    int serch;
    int set;
    int con;

    public MyList(String name, String item, String MAC, int icon, String con_state, int signal_state, int siran, int serch, int set, int con) {
        this.name = name;
        this.item = item;
        this.MAC = MAC;
        this.icon = icon;
        this.con_state = con_state;
        this.signal_state = signal_state;
        this.siran = siran;
        this.serch = serch;
        this.set = set;
        this.con = con;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMAC() { return MAC; }

    public void setMAC(String MAC) { this.MAC = MAC; }

    public String getCon_state() { return con_state; }

    public void setCon_state(String con_state) { this.con_state = con_state; }

    public int getSignal_state() { return signal_state; }

    public void setSignal_state(int signal_state) { this.signal_state = signal_state; }

    public int getSiran() { return siran; }

    public void setSiran(int siran) { this.siran = siran; }

    public int getSerch() { return serch; }

    public void setSerch(int serch) { this.serch = serch; }

    public int getSet() { return set; }

    public void setSet(int set) { this.set = set; }

    public int getCon() { return con; }

    public void setCon(int con) { this.con = con; }

    public int getIcon() { return icon; }

    public void setIcon(int icon) { this.icon = icon; }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
