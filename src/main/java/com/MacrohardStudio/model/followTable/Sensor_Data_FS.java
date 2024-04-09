package com.MacrohardStudio.model.followTable;

import java.sql.Timestamp;

public class Sensor_Data_FS
{
    private Integer id;
    private Integer device_id;
    private Timestamp time_stamp;

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
}
