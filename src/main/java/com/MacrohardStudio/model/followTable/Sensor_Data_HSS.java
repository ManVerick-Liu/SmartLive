package com.MacrohardStudio.model.followTable;

import java.sql.Timestamp;

public class Sensor_Data_HSS
{
    private Integer id;
    private Integer device_id;
    private Timestamp time_stamp;
    private Integer harmful_gas;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getDevice_id()
    {
        return device_id;
    }

    public void setDevice_id(Integer device_id)
    {
        this.device_id = device_id;
    }

    public Timestamp getTime_stamp()
    {
        return time_stamp;
    }

    public void setTime_stamp(Timestamp time_stamp)
    {
        this.time_stamp = time_stamp;
    }

    public Integer getHarmful_gas()
    {
        return harmful_gas;
    }

    public void setHarmful_gas(Integer harmful_gas)
    {
        this.harmful_gas = harmful_gas;
    }
}
