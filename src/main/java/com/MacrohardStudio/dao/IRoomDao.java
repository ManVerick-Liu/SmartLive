package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.Home_Room;
import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRoomDao
{
    public void add(Room room);
    public void addHome_Room(Home_Room home_room);
    public void modify(Room room);
    public Room select(Integer room_id);
    public List<Room> search(RoomDro roomDro);
}
