package com.geniteam.adeeb.sendsmschkdoulesim;

/**
 * Created by adeeb on 1/28/2018.
 */

class SimInfo {
     int id_;
    String display_name;
    String career_name;
    String countryIso;
     String icc_id;
  int slot;
    int subscriptionId;
    public SimInfo() {

    }

    public SimInfo(int id_, String display_name, String icc_id, int slot) {
        this.id_ = id_;
        this.display_name = display_name;
        this.icc_id = icc_id;
        this.slot = slot;
    }

    public int getId_() {
        return id_;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getIcc_id() {
        return icc_id;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "SimInfo{" +
                "id_=" + id_ +
                ", display_name='" + display_name + '\'' +
                ", icc_id='" + icc_id + '\'' +
                ", slot=" + slot +
                '}';
    }
}
