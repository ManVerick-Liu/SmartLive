package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.followTable.Room_Device;
import com.MacrohardStudio.model.rootTable.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDeviceDao
{
    public void add(Device device);
    public void addRoom_Device(Room_Device room_device);
    public void delete(Device device);
    public void deleteRoom_Device(Room_Device room_device);
    public void modify(Device device);
    public void modifyRoom_Device(Room_Device room_device);
    public List<Device> search(DeviceDro deviceDro);
    public Device searchById(Integer device_id);
}
