package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IDeviceDao;
import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.model.followTable.Room_Device;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseCode modify(DeviceDro deviceDro)
    {
        ResponseCode responseCode = new ResponseCode();

        Integer room_id = deviceDro.getRoom_id();

        if(room_id != null)
        {
            Room_Device room_device = new Room_Device();
            room_device.setDevice_id(deviceDro.getDevice_id());
            room_device.setRoom_id(room_id);

            iDeviceDao.modifyRoom_Device(room_device);
        }

        if(deviceDro.getDevice_name() != null)
        {
            Device device = new Device();
            device.setDevice_name(deviceDro.getDevice_name());
            device.setDevice_id(deviceDro.getDevice_id());

            iDeviceDao.modify(device);
        }

        if(room_id == null && deviceDro.getDevice_name() == null)
        {
            responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
            return  responseCode;
        }

        responseCode.setCode(HttpStatusCode.OK.getValue());

        return responseCode;
    }

    public ResponseData<List<Device>> search(Integer device_id, Integer room_id, String device_name, Device_Category device_category)
    {
        ResponseData<List<Device>> responseData = new ResponseData<>();

        DeviceDro deviceDro = new DeviceDro();
        deviceDro.setRoom_id(room_id);
        deviceDro.setDevice_category(device_category);
        deviceDro.setDevice_id(device_id);
        deviceDro.setDevice_name(device_name);

        List<Device> result = iDeviceDao.search(deviceDro);

        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);
        return responseData;
    }


}
