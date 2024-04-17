package com.MacrohardStudio.service.interfaces;


import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.followTable.*;
import com.MacrohardStudio.model.rootTable.Device;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public interface IDeviceService
{
    public ResponseCode control(Device_CommandDro device_commandDro);
    public Device searchByMacAddress(Integer device_mac_address);
    public ResponseCode add(DeviceDro deviceDro);
    public ResponseCode delete(Device device);
    public ResponseCode modify(DeviceDro deviceDro);
    public void updateDeviceActivation(Device device);
    public ResponseData<List<Device>> search(Integer device_id, Integer room_id, String device_name, Device_Category device_category, Integer device_activation);
    public List<Integer> searchDevice_IdByRoom_Id(Integer room_id);
    public Device searchDeviceByDevice_Id(Integer device_id);
    public Sensor_Data_TS searchSensor_Data_TS(Integer device_id);
    public Sensor_Data_HS searchSensor_Data_HS(Integer device_id);
    public Sensor_Data_CGS searchSensor_Data_CGS(Integer device_id);
    public Sensor_Data_HSS searchSensor_Data_HSS(Integer device_id);
    public Sensor_Data_HIS searchSensor_Data_HIS(Integer device_id);
    public Sensor_Data_FS searchSensor_Data_FS(Integer device_id);
}
