package com.MacrohardStudio.model;

public class Device {
    private Integer device_id;
    private String device_name;
    private String device_category;
    private String device_mac_address;

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

    public String getDevice_category() {
        return device_category;
    }

    public void setDevice_category(String device_category) {
        this.device_category = device_category;
    }

    public String getDevice_mac_address() {
        return device_mac_address;
    }

    public void setDevice_mac_address(String device_mac_address) {
        this.device_mac_address = device_mac_address;
    }
}
