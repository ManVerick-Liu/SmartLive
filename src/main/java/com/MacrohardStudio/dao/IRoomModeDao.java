package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.Mode;
import com.MacrohardStudio.model.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoomModeDao {
    public void change(Room room);
    public Mode select(Integer room_id);
}
