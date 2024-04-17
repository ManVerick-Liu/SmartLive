package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IRoomDao;
import com.MacrohardStudio.model.followTable.Home_Room;
import com.MacrohardStudio.model.rootTable.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.HttpStatusCode;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.model.enums.Room_Type;
import com.MacrohardStudio.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private IRoomDao iRoomDao;

    public ResponseData<Room> add(RoomDro roomDro)
    {
        Room room = new Room();
        room.setRoom_mode(Room_Mode.none);
        room.setRoom_type(roomDro.getRoom_type());
        room.setRoom_name(roomDro.getRoom_name());
        iRoomDao.add(room);

        Home_Room home_room = new Home_Room();
        home_room.setRoom_id(room.getRoom_id());
        home_room.setHome_id(roomDro.getHome_id());
        iRoomDao.addHome_Room(home_room);

        Room result = iRoomDao.select(room.getRoom_id());

        ResponseData<Room> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;

    }

    public ResponseCode modify(Room room)
    {
        iRoomDao.modify(room);

        ResponseCode responseCode = new ResponseCode();
        responseCode.setCode(HttpStatusCode.OK.getValue());

        return responseCode;
    }

    public ResponseData<List<Room>> search(Integer room_id, Room_Type room_type, String room_name, Integer home_id, Integer device_id)
    {
        RoomDro roomDro = new RoomDro();
        roomDro.setRoom_id(room_id);
        roomDro.setRoom_name(room_name);
        roomDro.setRoom_type(room_type);
        roomDro.setHome_id(home_id);
        roomDro.setDevice_id(device_id);

        List<Room> result = iRoomDao.search(roomDro);

        ResponseData<List<Room>> responseData = new ResponseData<>();
        responseData.setCode(HttpStatusCode.OK.getValue());
        responseData.setData(result);

        return responseData;
    }

    public Integer searchRoom_IdByDevice_Id(Integer device_id)
    {
        return iRoomDao.searchRoom_IdByDevice_Id(device_id);
    }
}
