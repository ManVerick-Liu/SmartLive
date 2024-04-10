package com.MacrohardStudio.service.interfaces;


import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.rootTable.Device;

public interface IDeviceService
{
    public ResponseCode add(DeviceDro deviceDro);
    public ResponseCode delete(Device device);

}
