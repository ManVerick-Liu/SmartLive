package com.MacrohardStudio.model.rootTable;

import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.model.enums.Room_Type;

public class Room
{
    private Integer room_id;
    private String room_name;
    private Room_Type room_type;
    private Room_Mode room_mode;

    public Integer getRoom_id()
    {
        return room_id;
    }

    public void setRoom_id(Integer room_id)
    {
        this.room_id = room_id;
    }

    public String getRoom_name()
    {
        return room_name;
    }

    public void setRoom_name(String room_name)
    {
        this.room_name = room_name;
    }

    public Room_Type getRoom_type()
    {
        return room_type;
    }

    public void setRoom_type(Room_Type room_type)
    {
        this.room_type = room_type;
    }

    public Room_Mode getRoom_mode()
    {
        return room_mode;
    }

    public void setRoom_mode(Room_Mode room_mode)
    {
        this.room_mode = room_mode;
    }
}
