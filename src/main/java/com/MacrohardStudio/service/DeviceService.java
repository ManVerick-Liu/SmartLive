package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IDeviceDao;
import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.model.followTable.Room_Device;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceService implements IDeviceService
{

    @Autowired
    private IDeviceDao iDeviceDao;


    public ResponseCode add(DeviceDro deviceDro)
    {
        Device device = new Device();
        device.setDevice_name(deviceDro.getDevice_name());
        device.setDevice_category(deviceDro.getDevice_category());
        device.setDevice_mac_address(deviceDro.getDevice_mac_address());
        iDeviceDao.add(device);

        Room_Device room_device = new Room_Device();
        //从插入的device获取device_id
        room_device.setDevice_id(device.getDevice_id());
        //从deviceDro获取room_id
        room_device.setRoom_id(deviceDro.getRoom_id());
        iDeviceDao.addRoom_Device(room_device);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());


        return responseCode;
    }


    public ResponseCode delete(Device device) {

        Room_Device room_device = new Room_Device();
        room_device.setDevice_id(device.getDevice_id());
        iDeviceDao.deleteRoom_Device(room_device);

        iDeviceDao.delete(device);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());
        return responseCode;
    }
}
