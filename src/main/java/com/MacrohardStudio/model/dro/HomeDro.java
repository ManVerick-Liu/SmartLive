package com.MacrohardStudio.model.dro;

public class HomeDro
{
    private Integer home_id;
    private String home_name;
    private String user_id;
    private Integer room_id;

    public Integer getRoom_id()
    {
        return room_id;
    }

    public void setRoom_id(Integer room_id)
    {
        this.room_id = room_id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public Integer getHome_id()
    {
        return home_id;
    }

    public void setHome_id(Integer home_id)
    {
        this.home_id = home_id;
    }

    public String getHome_name()
    {
        return home_name;
    }

    public void setHome_name(String home_name)
    {
        this.home_name = home_name;
    }
}
