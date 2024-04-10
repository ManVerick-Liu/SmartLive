package com.MacrohardStudio.model.dro;

import com.MacrohardStudio.model.enums.Device_Category;

public class DeviceDro {
    private Integer device_id;
    private String device_name;
    private Device_Category device_category;
    private String device_mac_address;
    private Integer room_id;

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public Device_Category getDevice_category() {
        return device_category;
    }

    public void setDevice_category(Device_Category device_category) {
        this.device_category = device_category;
    }

    public String getDevice_mac_address() {
        return device_mac_address;
    }

    public void setDevice_mac_address(String device_mac_address) {
        this.device_mac_address = device_mac_address;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }
}
