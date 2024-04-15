package com.MacrohardStudio.service.interfaces;


import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.rootTable.Device;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public interface IDeviceService
{
    public ResponseCode control(Device_CommandDro device_commandDro) throws JSONException;
    public Device searchByMacAddress(Integer device_mac_address);
    public ResponseCode add(DeviceDro deviceDro);
    public ResponseCode delete(Device device);
    public ResponseCode modify(DeviceDro deviceDro);
    public void updateDeviceActivation(Device device);
    public ResponseData<List<Device>> search(Integer device_id, Integer room_id, String device_name, Device_Category device_category, Integer device_activation);
}
