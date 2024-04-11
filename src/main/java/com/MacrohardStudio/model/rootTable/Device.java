package com.MacrohardStudio.model.rootTable;

import com.MacrohardStudio.model.enums.Device_Category;

public class Device {
    private Integer device_id;
    private String device_name;
    private Device_Category device_category;
    private Integer device_mac_address;
    private Integer device_activation;

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

    public Integer getDevice_mac_address() {
        return device_mac_address;
    }

    public void setDevice_mac_address(Integer device_mac_address) {
        this.device_mac_address = device_mac_address;
    }

    public Device_Category getDevice_category()
    {
        return device_category;
    }

    public void setDevice_category(Device_Category device_category)
    {
        this.device_category = device_category;
    }

    public Integer getDevice_activation()
    {
        return device_activation;
    }

    public void setDevice_activation(Integer device_activation)
    {
        this.device_activation = device_activation;
    }
}
