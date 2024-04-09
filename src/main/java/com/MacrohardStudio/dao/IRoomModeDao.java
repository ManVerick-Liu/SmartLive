package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.Mode;
import com.MacrohardStudio.model.enums.Room_Mode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoomModeDao {
    public void change(Integer room_id, Room_Mode room_mode);
    public Mode select(Integer room_id);
}
