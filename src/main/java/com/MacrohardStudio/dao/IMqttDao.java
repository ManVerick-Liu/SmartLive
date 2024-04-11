package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.followTable.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMqttDao
{
    public void addSensor_Data_CGS(Sensor_Data_CGS sensor_data_cgs);
    public void addSensor_Data_CS(Sensor_Data_CS sensor_data_CS);
    public void addSensor_Data_FS(Sensor_Data_FS sensor_data_fs);
    public void addSensor_Data_HIS(Sensor_Data_HIS sensor_data_his);
    public void addSensor_Data_HS(Sensor_Data_HS sensor_data_hs);
    public void addSensor_Data_HSS(Sensor_Data_HSS sensor_data_hss);
    public void addSensor_Data_LS(Sensor_Data_LS sensor_data_ls);
    public void addSensor_Data_TS(Sensor_Data_TS sensor_data_ts);
}
