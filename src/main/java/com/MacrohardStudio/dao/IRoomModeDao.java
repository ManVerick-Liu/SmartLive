package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.rootTable.Mode;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.model.rootTable.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoomModeDao {
    public void change(Room room);
    public Mode select(Integer room_id);
}
