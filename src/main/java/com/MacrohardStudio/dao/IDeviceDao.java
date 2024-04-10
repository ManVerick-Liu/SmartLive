package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.followTable.Room_Device;
import com.MacrohardStudio.model.rootTable.Device;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDeviceDao
{
    public void add(Device device);
    public void addRoom_Device(Room_Device room_device);
    public void delete(Device device);
    public void deleteRoom_Device(Room_Device room_device);


}
