package com.MacrohardStudio.model.followTable;

import com.MacrohardStudio.model.rootTable.Device;

public class Room_Device
{
    private Integer id;
    private Integer room_id;
    private Integer device_id;
    private Device device;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getRoom_id()
    {
        return room_id;
    }

    public void setRoom_id(Integer room_id)
    {
        this.room_id = room_id;
    }

    public Integer getDevice_id()
    {
        return device_id;
    }

    public void setDevice_id(Integer device_id)
    {
        this.device_id = device_id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
