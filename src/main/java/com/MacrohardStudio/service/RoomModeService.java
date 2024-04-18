package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IRoomModeDao;
import com.MacrohardStudio.model.dro.Device_Command;
import com.MacrohardStudio.model.dro.Device_CommandDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.*;
import com.MacrohardStudio.model.rootTable.Device;
import com.MacrohardStudio.model.rootTable.Room;
import com.MacrohardStudio.service.interfaces.IDeviceService;
import com.MacrohardStudio.service.interfaces.IRoomModeService;
import com.MacrohardStudio.service.interfaces.IRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RoomModeService implements IRoomModeService {

    @Autowired
    private IRoomModeDao iRoomModeDao;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IRoomService iRoomService;

    // 每隔10秒执行一次
    @Async
    @Scheduled(fixedRate = 10000)
    public void roomModeHandler()
    {
        log.info(LogTitle.Main + " 开始执行定时任务：房间模式监测");
        List<Room> rooms = iRoomService.searchAllRoom();

        for (Room room : rooms)
        {
            Room_Mode room_mode = room.getRoom_mode();
            Integer room_id = room.getRoom_id();
            switch (room_mode)
            {
                case purification:
                {
                    purificationModeHandler(room_id);
                    break;
                }
                case sleep:
                {
                    sleepModeHandler(room_id);
                    break;
                }
                case study:
                {
                    studyModeHandler(room_id);
                    break;
                }
                case entertainment:
                {
                    entertainmentModeHandler(room_id);
                    break;
                }
                case security:
                {
                    securityModeHandler(room_id);
                    break;
                }
                case meal:
                {
                    mealModeHandler(room_id);
                    break;
                }
                case none:
                {
                    //noneModeHandler(room_id);
                    break;
                }
            }
        }
    }

    public ResponseCode change(Room room)
    {
        Integer room_id = room.getRoom_id();
        Room_Mode room_mode = room.getRoom_mode();
        ResponseCode responseCode = new ResponseCode();

        iRoomModeDao.change(room);
        switch (room_mode)
        {
            case purification:
            {
                purificationModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case sleep:
            {
                sleepModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case study:
            {
                studyModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case entertainment:
            {
                entertainmentModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case security:
            {
                securityModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case meal:
            {
                mealModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
            case none:
            {
                noneModeHandler(room_id);
                responseCode.setCode(HttpStatusCode.OK.getValue());
                break;
            }
        }

        return responseCode;
    }

    public void purificationModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> ts_ids = new ArrayList<>();
        List<Integer> hs_ids = new ArrayList<>();
        List<Integer> ac_ids = new ArrayList<>();
        List<Integer> fan_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if(device.getDevice_category() == Device_Category.HS)
            {
                hs_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.TS)
            {
                ts_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.AC)
            {
                ac_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.Fan)
            {
                fan_ids.add(device.getDevice_id());
            }
        }

        if(ts_ids.isEmpty() || hs_ids.isEmpty())
        {
            log.warn(LogTitle.Main.toString() + " 房间 {} 没有温度或者湿度传感器，新风系统将无法正常工作", room_id);
            return;
        }

        List<Float> temperatureList = new ArrayList<>();
        List<Float> humidityList = new ArrayList<>();

        for (Integer hs_id : hs_ids)
        {
            humidityList.add(iDeviceService.searchSensor_Data_HS(hs_id).getHumidity());
        }
        for (Integer ts_id : ts_ids)
        {
            temperatureList.add(iDeviceService.searchSensor_Data_TS(ts_id).getTemperature());
        }

        float temperatureAverage = (float) calculateAverage(temperatureList);
        float humidityAverage = (float) calculateAverage(humidityList);

        Device_CommandDro device_commandDro_AC = new Device_CommandDro();
        Device_Command device_command_AC = new Device_Command();

        Device_CommandDro device_commandDro_Fan = new Device_CommandDro();
        Device_Command device_command_Fan = new Device_Command();

        if (temperatureAverage < 10 && humidityAverage < 50)
        {
            device_command_AC.setCommand(1);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(1);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }

        }
        else if (temperatureAverage < 10 && humidityAverage > 50)
        {
            device_command_AC.setCommand(1);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(0);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if (temperatureAverage > 10 && temperatureAverage < 20 && humidityAverage < 50)
        {
            device_command_AC.setCommand(2);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(2);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if (temperatureAverage > 10 && temperatureAverage < 20 && humidityAverage > 50)
        {
            device_command_AC.setCommand(2);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(1);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if (temperatureAverage > 20 && temperatureAverage < 30 && humidityAverage < 50)
        {
            device_command_AC.setCommand(3);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(2);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if (temperatureAverage > 20 && temperatureAverage < 30 && humidityAverage > 50)
        {
            device_command_AC.setCommand(3);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(1);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if (temperatureAverage > 30 && humidityAverage < 50)
        {
            device_command_AC.setCommand(3);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(3);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
        else if(temperatureAverage > 30 && humidityAverage > 50)
        {
            device_command_AC.setCommand(3);
            device_commandDro_AC.setDevice_command(device_command_AC);

            for (Integer AC_id : ac_ids)
            {
                device_commandDro_AC.setDevice_id(AC_id);
                iDeviceService.control(device_commandDro_AC);
            }

            device_command_Fan.setCommand(2);
            device_commandDro_Fan.setDevice_command(device_command_Fan);

            for (Integer Fan_id : fan_ids)
            {
                device_commandDro_Fan.setDevice_id(Fan_id);
                iDeviceService.control(device_commandDro_Fan);
            }
        }
    }

    public void sleepModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> led_ids = new ArrayList<>();
        List<Integer> displayer_ids = new ArrayList<>();
        List<Integer> audio_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if (device.getDevice_category() == Device_Category.LED)
            {
                led_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Displayer)
            {
                displayer_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Audio)
            {
                audio_ids.add(device.getDevice_id());
            }
        }

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer led_id : led_ids)
        {
            device_commandDro.setDevice_id(led_id);
            device_command.setCommand(1);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer displayer_id : displayer_ids)
        {
            device_commandDro.setDevice_id(displayer_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer audio_id : audio_ids)
        {
            device_commandDro.setDevice_id(audio_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
    }

    public void studyModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> led_ids = new ArrayList<>();
        List<Integer> displayer_ids = new ArrayList<>();
        List<Integer> audio_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if (device.getDevice_category() == Device_Category.LED)
            {
                led_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Displayer)
            {
                displayer_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Audio)
            {
                audio_ids.add(device.getDevice_id());
            }
        }

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer led_id : led_ids)
        {
            device_commandDro.setDevice_id(led_id);
            device_command.setCommand(2);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer displayer_id : displayer_ids)
        {
            device_commandDro.setDevice_id(displayer_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer audio_id : audio_ids)
        {
            device_commandDro.setDevice_id(audio_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
    }

    public void entertainmentModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> led_ids = new ArrayList<>();
        List<Integer> displayer_ids = new ArrayList<>();
        List<Integer> audio_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if (device.getDevice_category() == Device_Category.LED)
            {
                led_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Displayer)
            {
                displayer_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Audio)
            {
                audio_ids.add(device.getDevice_id());
            }
        }

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer led_id : led_ids)
        {
            device_commandDro.setDevice_id(led_id);
            device_command.setCommand(111);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer displayer_id : displayer_ids)
        {
            device_commandDro.setDevice_id(displayer_id);
            device_command.setCommand(1);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer audio_id : audio_ids)
        {
            device_commandDro.setDevice_id(audio_id);
            device_command.setCommand(1);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
    }
    public void noneModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        //List<Device> devices = new ArrayList<>();

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer device_id : device_ids)
        {
            device_commandDro.setDevice_id(device_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
            //devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        /*List<Integer> led_ids = new ArrayList<>();
        List<Integer> displayer_ids = new ArrayList<>();
        List<Integer> audio_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if (device.getDevice_category() == Device_Category.LED)
            {
                led_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Displayer)
            {
                displayer_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Audio)
            {
                audio_ids.add(device.getDevice_id());
            }
        }

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer led_id : led_ids)
        {
            device_commandDro.setDevice_id(led_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer displayer_id : displayer_ids)
        {
            device_commandDro.setDevice_id(displayer_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer audio_id : audio_ids)
        {
            device_commandDro.setDevice_id(audio_id);
            device_command.setCommand(0);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }*/
    }

    public void securityModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> cgs_ids = new ArrayList<>();
        List<Integer> hss_ids = new ArrayList<>();
        List<Integer> his_ids = new ArrayList<>();
        List<Integer> fs_ids = new ArrayList<>();
        List<Integer> buzzer_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if(device.getDevice_category() == Device_Category.CGS)
            {
                cgs_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.HSS)
            {
                hss_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.HIS)
            {
                his_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.FS)
            {
                fs_ids.add(device.getDevice_id());
            }
            if(device.getDevice_category() == Device_Category.Buzzer)
            {
                buzzer_ids.add(device.getDevice_id());
            }
        }

        //可以检查房间中是否有对应设备
        if(cgs_ids.isEmpty())
        {
            log.info(LogTitle.Main.toString() + " 该房间没有可燃气体传感器");
        }
        if(hss_ids.isEmpty())
        {
            log.info(LogTitle.Main.toString() + " 该房间没有有害气体传感器");
        }
        if(his_ids.isEmpty())
        {
            log.info(LogTitle.Main.toString() + " 该房间没有人体感应传感器");
        }
        if(fs_ids.isEmpty())
        {
            log.info(LogTitle.Main.toString() + " 该房间没有火焰传感器");
        }
        if(buzzer_ids.isEmpty())
        {
            log.info(LogTitle.Main.toString() + " 该房间没有蜂鸣器");
        }
        //

        List<Integer> cgs_data_list = new ArrayList<>();
        List<Integer> hss_data_list = new ArrayList<>();
        List<HIS_Detection> his_data_list = new ArrayList<>();
        List<Fire_Detection> fs_data_list = new ArrayList<>();

        for (Integer cgs_id : cgs_ids)
        {
            cgs_data_list.add(iDeviceService.searchSensor_Data_CGS(cgs_id).getCombustible_gas());
        }
        for (Integer hss_id : hss_ids)
        {
            hss_data_list.add(iDeviceService.searchSensor_Data_HSS(hss_id).getHarmful_gas());
        }
        for (Integer his_id : his_ids)
        {
            his_data_list.add(iDeviceService.searchSensor_Data_HIS(his_id).getHis_detection());
        }
        for (Integer fs_id : fs_ids)
        {
            fs_data_list.add(iDeviceService.searchSensor_Data_FS(fs_id).getFire_detection());
        }

        Integer cgs_data_average = (int) calculateAverage(cgs_data_list);
        Integer hss_data_average = (int) calculateAverage(hss_data_list);
        HIS_Detection his_detection = HIS_Detection.undetected;
        Fire_Detection fire_detection = Fire_Detection.normal;

        for (HIS_Detection his_detection_temp : his_data_list)
        {
            if (his_detection_temp == HIS_Detection.detected)
            {
                his_detection = HIS_Detection.detected;
            }
        }
        for (Fire_Detection fire_detection_temp : fs_data_list)
        {
            if (fire_detection_temp == Fire_Detection.fire)
            {
                fire_detection = Fire_Detection.fire;
            }
        }

        if (cgs_data_average > 400 || hss_data_average > 400 || his_detection == HIS_Detection.detected || fire_detection == Fire_Detection.fire)
        {
            log.info(LogTitle.Main + " 安防系统启动，当前检测到危险");
            for (Integer buzzer_id : buzzer_ids)
            {
                Device_CommandDro device_commandDro = new Device_CommandDro();
                Device_Command device_command = new Device_Command();
                device_commandDro.setDevice_id(buzzer_id);
                device_command.setCommand(1);
                device_commandDro.setDevice_command(device_command);
                iDeviceService.control(device_commandDro);
            }
        }
        else log.info(LogTitle.Main + " 安防系统启动，当前并未检测到危险");
    }

    public void mealModeHandler(Integer room_id)
    {
        List<Integer> device_ids = iDeviceService.searchDevice_IdByRoom_Id(room_id);
        List<Device> devices = new ArrayList<>();
        for (Integer device_id : device_ids)
        {
            devices.add(iDeviceService.searchDeviceByDevice_Id(device_id));
        }

        List<Integer> led_ids = new ArrayList<>();
        List<Integer> displayer_ids = new ArrayList<>();
        List<Integer> audio_ids = new ArrayList<>();
        for (Device device : devices)
        {
            if (device.getDevice_category() == Device_Category.LED)
            {
                led_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Displayer)
            {
                displayer_ids.add(device.getDevice_id());
            }
            if (device.getDevice_category() == Device_Category.Audio)
            {
                audio_ids.add(device.getDevice_id());
            }
        }

        Device_CommandDro device_commandDro = new Device_CommandDro();
        Device_Command device_command = new Device_Command();

        for (Integer led_id : led_ids)
        {
            device_commandDro.setDevice_id(led_id);
            device_command.setCommand(3);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer displayer_id : displayer_ids)
        {
            device_commandDro.setDevice_id(displayer_id);
            device_command.setCommand(1);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
        for (Integer audio_id : audio_ids)
        {
            device_commandDro.setDevice_id(audio_id);
            device_command.setCommand(1);
            device_commandDro.setDevice_command(device_command);
            iDeviceService.control(device_commandDro);
        }
    }

    //计算 List 中数据的平均值（泛型方法）
    public static <T extends Number> double calculateAverage(List<T> list)
    {
        if (list.isEmpty()) {
            return 0; // 如果 List 为空，返回0
        }

        double sum = 0;
        for (T num : list) {
            sum += num.doubleValue();
        }

        return sum / list.size();
    }
}
