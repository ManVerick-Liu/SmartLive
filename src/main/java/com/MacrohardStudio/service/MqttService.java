package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IMqttDao;
import com.MacrohardStudio.model.enums.Device_Category;
import com.MacrohardStudio.model.enums.Fire_Detection;
import com.MacrohardStudio.model.enums.HIS_Detection;
import com.MacrohardStudio.model.followTable.*;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import com.MacrohardStudio.service.interfaces.IMqttService;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

@Service
public class MqttService implements IMqttService
{
    @Autowired
    private IMqttDao iMqttDao;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private SenderClient senderClient;


    public void publish(String device_mac_address, String command) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(device_mac_address, command);
        senderClient.publish("subtopic", jsonObject.toString());
    }

    public void mqttMessageHandler(JSONObject data) throws JSONException
    {
        Iterator<String> keyIterator = data.keys();
        String key = keyIterator.next();
        Integer device_mac_address = Integer.parseInt(key);
        Device device = iDeviceService.searchByMacAddress(device_mac_address);
        Device_Category device_category = device.getDevice_category();
        Integer device_id = device.getDevice_id();

        //获取当前日期时间
        Date currentDate = new Date();

        //将日期时间转换为 SQL 时间戳类型
        Timestamp timeStamp = new Timestamp(currentDate.getTime());

        switch (device_category)
        {
            //非传感器类
            case LED:
            {

                break;
            }

            case Buzzer:
            {

                break;
            }

            case Displayer:
            {

                break;
            }

            case AC:
            {

                break;
            }

            case Fan:
            {

                break;
            }

            case Audio:
            {

                break;
            }

            //传感器类
            case CGS:
            {
                Sensor_Data_CGS sensor_data_cgs = new Sensor_Data_CGS();
                sensor_data_cgs.setDevice_id(device_id);
                sensor_data_cgs.setTime_stamp(timeStamp);
                sensor_data_cgs.setCombustible_gas(data.getInt(key));
                iMqttDao.addSensor_Data_CGS(sensor_data_cgs);
                break;
            }

            case HIS:
            {
                if(HIS_Detection.valueOf(data.getString(key)) != HIS_Detection.detected)
                {
                    break;
                }
                Sensor_Data_HIS sensor_data_his = new Sensor_Data_HIS();
                sensor_data_his.setDevice_id(device_id);
                sensor_data_his.setTime_stamp(timeStamp);
                sensor_data_his.setHis_detection(HIS_Detection.valueOf(data.getString(key)));
                iMqttDao.addSensor_Data_HIS(sensor_data_his);
                break;
            }

            case TS:
            {
                Sensor_Data_TS sensor_data_ts = new Sensor_Data_TS();
                sensor_data_ts.setDevice_id(device_id);
                sensor_data_ts.setTime_stamp(timeStamp);
                sensor_data_ts.setTemperature((float) data.getDouble(key));
                iMqttDao.addSensor_Data_TS(sensor_data_ts);
                break;
            }

            case HS:
            {
                Sensor_Data_HS sensor_data_hs = new Sensor_Data_HS();
                sensor_data_hs.setDevice_id(device_id);
                sensor_data_hs.setTime_stamp(timeStamp);
                sensor_data_hs.setHumidity((float) data.getDouble(key));
                iMqttDao.addSensor_Data_HS(sensor_data_hs);
                break;
            }

            case CS:
            {
                Sensor_Data_CS sensor_data_cs = new Sensor_Data_CS();
                sensor_data_cs.setDevice_id(device_id);
                sensor_data_cs.setTime_stamp(timeStamp);
                sensor_data_cs.setRgb(data.getString(key));
                iMqttDao.addSensor_Data_CS(sensor_data_cs);
                break;
            }

            case LS:
            {
                Sensor_Data_LS sensor_data_ls = new Sensor_Data_LS();
                sensor_data_ls.setDevice_id(device_id);
                sensor_data_ls.setTime_stamp(timeStamp);
                sensor_data_ls.setLuminosity(data.getInt(key));
                iMqttDao.addSensor_Data_LS(sensor_data_ls);
                break;
            }

            case HSS:
            {
                Sensor_Data_HSS sensor_data_hss = new Sensor_Data_HSS();
                sensor_data_hss.setDevice_id(device_id);
                sensor_data_hss.setTime_stamp(timeStamp);
                sensor_data_hss.setHarmful_gas(data.getInt(key));
                iMqttDao.addSensor_Data_HSS(sensor_data_hss);
                break;
            }

            case FS:
            {
                if(Fire_Detection.valueOf(data.getString(key)) != Fire_Detection.fire)
                {
                    break;
                }
                Sensor_Data_FS sensor_data_fs = new Sensor_Data_FS();
                sensor_data_fs.setDevice_id(device_id);
                sensor_data_fs.setTime_stamp(timeStamp);
                sensor_data_fs.setFire_detection(Fire_Detection.valueOf(data.getString(key)));
                iMqttDao.addSensor_Data_FS(sensor_data_fs);
                break;
            }

            //未知设备类
            case Unknown:
            {

                break;
            }
        }
    }
}
