package com.MacrohardStudio.service.interfaces;


import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.rootTable.Device;

import java.util.List;

public interface IDeviceService
{
    public ResponseCode add(DeviceDro deviceDro);
    public ResponseCode delete(Device device);
    public ResponseCode modify(DeviceDro deviceDro);
    public ResponseData<List<Device>> search(Integer device_id, Integer room_id, String device_name, Device_Category device_category);
}
