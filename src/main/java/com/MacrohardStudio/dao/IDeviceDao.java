package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.followTable.*;
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
    public void updateDeviceActivation(Device device);
    public Device searchByMacAddress(Integer device_mac_address);
    public List<Device> searchDeviceByRoom_Id(Integer room_id);
    public Sensor_Data_TS searchSensor_Data_TS(Integer device_id);
    public Sensor_Data_HS searchSensor_Data_HS(Integer device_id);
    public Sensor_Data_CGS searchSensor_Data_CGS(Integer device_id);
    public Sensor_Data_HSS searchSensor_Data_HSS(Integer device_id);
    public Sensor_Data_HIS searchSensor_Data_HIS(Integer device_id);
    public Sensor_Data_FS searchSensor_Data_FS(Integer device_id);
}
