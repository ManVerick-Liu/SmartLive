package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Room_Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IRoomService
{
    public ResponseData<Room> add(RoomDro roomDro);
    public ResponseData<List<Room>> search(Integer room_id, Room_Type room_type, String room_name, Integer home_id, Integer device_id);
    public ResponseCode modify(Room room);
}