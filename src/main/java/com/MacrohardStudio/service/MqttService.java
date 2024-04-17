package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IMqttDao;
import com.MacrohardStudio.model.enums.*;
import com.MacrohardStudio.model.followTable.*;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import com.MacrohardStudio.service.interfaces.IMqttService;
import com.MacrohardStudio.service.interfaces.IRoomService;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import com.MacrohardStudio.utilities.webSocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@Slf4j
@Service
public class MqttService implements IMqttService
{
    @Autowired
    private IMqttDao iMqttDao;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private SenderClient senderClient;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private IRoomService iRoomService;

    @Async
    public void publish(String device_mac_address, String command)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(device_mac_address, command);
            senderClient.publish("subtopic", jsonObject.toString());
        }
        catch (JSONException e)
        {
            log.error(LogTitle.MQTT.toString() + " 发送消息失败");
            e.printStackTrace();
        }
    }

    @Async
    public void mqttMessageHandler(JSONObject data) throws JSONException
    {
        Iterator<String> keyIterator = data.keys();
        String key = keyIterator.next();
        Integer device_mac_address = Integer.parseInt(key);

        Device device = iDeviceService.searchByMacAddress(device_mac_address);
        if (device == null)
        {
            log.info(LogTitle.MQTT.toString() + " 设备 {} 未被纳入系统管理，无法处理数据", device_mac_address);
            return;
        }

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
                //更新设备状态
                if(Objects.equals(data.getString(key), "000"))
                {
                    this.updateDevice_Activation(device, 0);
                }
                else this.updateDevice_Activation(device, data.getInt(key));

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            case Buzzer:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            case Displayer:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            case AC:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            case Fan:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            case Audio:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        data.getString(key),
                        data.getString(key));

                break;
            }

            //传感器类
            case CGS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_CGS sensor_data_cgs = new Sensor_Data_CGS();
                sensor_data_cgs.setDevice_id(device_id);
                sensor_data_cgs.setTime_stamp(timeStamp);
                sensor_data_cgs.setCombustible_gas(data.getInt(key));
                iMqttDao.addSensor_Data_CGS(sensor_data_cgs);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case HIS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                if(HIS_Detection.valueOf(data.getString(key)) != HIS_Detection.detected)
                {
                    break;
                }
                Sensor_Data_HIS sensor_data_his = new Sensor_Data_HIS();
                sensor_data_his.setDevice_id(device_id);
                sensor_data_his.setTime_stamp(timeStamp);
                sensor_data_his.setHis_detection(HIS_Detection.valueOf(data.getString(key)));
                iMqttDao.addSensor_Data_HIS(sensor_data_his);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case TS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_TS sensor_data_ts = new Sensor_Data_TS();
                sensor_data_ts.setDevice_id(device_id);
                sensor_data_ts.setTime_stamp(timeStamp);
                sensor_data_ts.setTemperature((float) data.getDouble(key));
                iMqttDao.addSensor_Data_TS(sensor_data_ts);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case HS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_HS sensor_data_hs = new Sensor_Data_HS();
                sensor_data_hs.setDevice_id(device_id);
                sensor_data_hs.setTime_stamp(timeStamp);
                sensor_data_hs.setHumidity((float) data.getDouble(key));
                iMqttDao.addSensor_Data_HS(sensor_data_hs);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case CS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_CS sensor_data_cs = new Sensor_Data_CS();
                sensor_data_cs.setDevice_id(device_id);
                sensor_data_cs.setTime_stamp(timeStamp);
                sensor_data_cs.setRgb(data.getString(key));
                iMqttDao.addSensor_Data_CS(sensor_data_cs);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case LS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_LS sensor_data_ls = new Sensor_Data_LS();
                sensor_data_ls.setDevice_id(device_id);
                sensor_data_ls.setTime_stamp(timeStamp);
                sensor_data_ls.setLuminosity(data.getInt(key));
                iMqttDao.addSensor_Data_LS(sensor_data_ls);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case HSS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                Sensor_Data_HSS sensor_data_hss = new Sensor_Data_HSS();
                sensor_data_hss.setDevice_id(device_id);
                sensor_data_hss.setTime_stamp(timeStamp);
                sensor_data_hss.setHarmful_gas(data.getInt(key));
                iMqttDao.addSensor_Data_HSS(sensor_data_hss);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            case FS:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //数据持久化
                if(Fire_Detection.valueOf(data.getString(key)) != Fire_Detection.fire)
                {
                    break;
                }
                Sensor_Data_FS sensor_data_fs = new Sensor_Data_FS();
                sensor_data_fs.setDevice_id(device_id);
                sensor_data_fs.setTime_stamp(timeStamp);
                sensor_data_fs.setFire_detection(Fire_Detection.valueOf(data.getString(key)));
                iMqttDao.addSensor_Data_FS(sensor_data_fs);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }

            //未知设备类
            case Unknown:
            {
                if (data.getInt(key) != 0)
                {
                    //更新设备状态
                    this.updateDevice_Activation(device, 1);
                }
                else this.updateDevice_Activation(device, 0);

                //通过WebSocket进行数据转发
                this.wrapToJsonAndSendWebSocketMessage(
                        WebSocket_Message_Type.device_data_transfer,
                        device_id,
                        device_category,
                        "1",
                        data.getString(key));

                break;
            }
        }
    }

    private void updateDevice_Activation(Device device, Integer activation)
    {
        device.setDevice_activation(activation);
        iDeviceService.updateDeviceActivation(device);
    }

    private void wrapToJsonAndSendWebSocketMessage
            (WebSocket_Message_Type webSocket_message_type,
             Integer device_id,
             Device_Category device_category,
             String status,
             String device_data) throws JSONException
    {
        JSONObject transferData = new JSONObject();
        JSONObject dataJson = new JSONObject();
        transferData.put("message_type", webSocket_message_type);
        transferData.put("device_id", device_id);
        transferData.put("device_category", device_category);
        transferData.put("room_id", iRoomService.searchRoom_IdByDevice_Id(device_id));
        dataJson.put("status", status);
        dataJson.put("device_data", device_data);
        transferData.put("data", dataJson);

        webSocketService.sendToAll(transferData.toString());
    }
}
