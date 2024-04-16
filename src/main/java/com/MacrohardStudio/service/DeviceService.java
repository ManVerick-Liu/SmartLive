package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IDeviceDao;
import com.MacrohardStudio.model.dro.DeviceDro;
import com.MacrohardStudio.model.dro.Device_Command;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.model.followTable.*;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import com.MacrohardStudio.service.interfaces.IMqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.*;

@Slf4j
@Service
public class DeviceService implements IDeviceService
{
    @Autowired
    private IDeviceDao iDeviceDao;

    @Autowired
    private IMqttService iMqttService;

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

    public ResponseData<List<Device>> search(Integer device_id, Integer room_id, String device_name, Device_Category device_category, Integer device_activation)
    {
        ResponseData<List<Device>> responseData = new ResponseData<>();

        DeviceDro deviceDro = new DeviceDro();
        deviceDro.setRoom_id(room_id);
        deviceDro.setDevice_category(device_category);
        deviceDro.setDevice_id(device_id);
        deviceDro.setDevice_name(device_name);
        deviceDro.setDevice_activation(device_activation);

        List<Device> result = iDeviceDao.search(deviceDro);

        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);
        return responseData;
    }

    public ResponseCode control(Device_CommandDro device_commandDro)
    {
        //先声明并初始化一些变量，如整个接口的返回值、控制命令、设备实体类
        ResponseCode responseCode = new ResponseCode();
        Device device = this.iDeviceDao.searchById(device_commandDro.getDevice_id());
        Integer device_mac_address = device.getDevice_mac_address();
        Integer command = device_commandDro.getDevice_command().getCommand();

        //取出设备的类型，并对其加以判断
        Device_Category device_category = device.getDevice_category();

        //当设备是LED灯时进入此分支
        if(device_category == Device_Category.LED)
        {

            String rgbData = device_commandDro.getDevice_command().getRgb();
            //判断命令是否为空
            if(command != null)
            {
                //判断当前设备状态必须是与控制命令互斥的，否则打回请求
                /*if(command > 0 && command != 9 && device.getDevice_activation() == 9)
                {
                    command = 1;
                    iMqttService.publish(device_mac_address.toString(), command.toString());
                    //device.setDevice_activation(1);
                    //updateDeviceActivation(device);
                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else if(command == 0 && device.getDevice_activation() > 0)
                {
                    iMqttService.publish(device_mac_address.toString(), command.toString());
                    //device.setDevice_activation(1);
                    //updateDeviceActivation(device);
                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else
                {
                    responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
                    return responseCode;
                }*/

                //不检查命令与设备状态的互斥
                iMqttService.publish(device_mac_address.toString(), command.toString());
                //device.setDevice_activation(1);
                //updateDeviceActivation(device);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                return responseCode;
            }

            if(rgbData != null)
            {
                //如果设备不在工作，打回请求
                if(device.getDevice_activation() == 0)
                {
                    responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
                    return responseCode;
                }
                else
                {
                    //检查RGB数据是否符合格式要求
                    //定义符合RGB格式的正则表达式
                    String rgbPattern = "\\((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)," + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)," + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\)";

                    //使用正则表达式进行匹配
                    Pattern pattern = Pattern.compile(rgbPattern);
                    Matcher matcher = pattern.matcher(rgbData);

                    //判断匹配结果
                    if(matcher.matches())
                    {
                        iMqttService.publish(device_mac_address.toString(), rgbData);
                        //device.setDevice_activation(1);
                        //updateDeviceActivation(device);
                        responseCode.setCode(HttpStatusCode.OK.getValue());
                        return responseCode;
                    }
                    else
                    {
                        responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
                        return responseCode;
                    }
                }
            }
        }

        //当设备是空调或者风扇时，进入此分支
        else if (device_category == Device_Category.AC || device_category == Device_Category.Fan)
        {
            if(command != null)
            {
                //判断当前设备状态必须是与控制命令互斥的，否则打回请求
                /*if(command > 0 && command != 9 && device.getDevice_activation() == 9)
                {
                    iMqttService.publish(device_mac_address.toString(), command.toString());

                    device.setDevice_activation(command);
                    updateDeviceActivation(device);

                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else if(command == 9 && device.getDevice_activation() != 9)
                {
                    iMqttService.publish(device_mac_address.toString(), command.toString());

                    device.setDevice_activation(command);
                    updateDeviceActivation(device);

                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else
                {
                    responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
                    return responseCode;
                }*/

                //不检查命令与设备状态的互斥
                iMqttService.publish(device_mac_address.toString(), command.toString());
                //device.setDevice_activation(1);
                //updateDeviceActivation(device);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                return responseCode;
            }
        }

        //当设备不属于上面的设备类型，且也不是未知设备时，进入此分支
        else if(device_category != Device_Category.Unknown)
        {
            if(command != null)
            {
                //判断当前设备状态必须是与控制命令互斥的，否则打回请求
                /*if(command > 0 && command != 9 && device.getDevice_activation() == 9)
                {
                    iMqttService.publish(device_mac_address.toString(), command.toString());

                    device.setDevice_activation(command);
                    updateDeviceActivation(device);

                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else if(command == 9 && device.getDevice_activation() != 9)
                {
                    iMqttService.publish(device_mac_address.toString(), command.toString());

                    device.setDevice_activation(command);
                    updateDeviceActivation(device);

                    responseCode.setCode(HttpStatusCode.OK.getValue());
                    return responseCode;
                }
                else
                {
                    responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
                    return responseCode;
                }*/

                //不检查命令与设备状态的互斥
                iMqttService.publish(device_mac_address.toString(), command.toString());
                //device.setDevice_activation(1);
                //updateDeviceActivation(device);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                return responseCode;
            }
        }

        responseCode.setCode(HttpStatusCode.BAD_REQUEST.getValue());
        return responseCode;
    }

    public Device searchByMacAddress(Integer device_mac_address)
    {
        return iDeviceDao.searchByMacAddress(device_mac_address);
    }

    public void updateDeviceActivation(Device device)
    {
        iDeviceDao.updateDeviceActivation(device);
    }

    public List<Device> searchDeviceByRoom_Id(Integer room_id)
    {
        return iDeviceDao.searchDeviceByRoom_Id(room_id);
    }

    public Sensor_Data_TS searchSensor_Data_TS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_TS(device_id);
    }

    public Sensor_Data_HS searchSensor_Data_HS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_HS(device_id);
    }

    public Sensor_Data_CGS searchSensor_Data_CGS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_CGS(device_id);
    }

    public Sensor_Data_HSS searchSensor_Data_HSS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_HSS(device_id);
    }

    public Sensor_Data_HIS searchSensor_Data_HIS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_HIS(device_id);
    }

    public Sensor_Data_FS searchSensor_Data_FS(Integer device_id)
    {
        return iDeviceDao.searchSensor_Data_FS(device_id);
    }
}
