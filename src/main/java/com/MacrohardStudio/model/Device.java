package com.MacrohardStudio.model;

public class Device {
    private Integer device_id;
    private String device_name;
    private String category;
    private String MAC_address;


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMAC_address() {
        return MAC_address;
    }

    public void setMAC_address(String MAC_address) {
        this.MAC_address = MAC_address;
    }
}
