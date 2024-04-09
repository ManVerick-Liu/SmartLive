package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IRoomModeDao;
import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.service.interfaces.IRoomModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoomModeService implements IRoomModeService {

    @Autowired
    private IRoomModeDao iRoomModeDao;

    public ResponseEntity<Integer> change(Room room) {

        System.out.println(room.getRoom_type());
        iRoomModeDao.change(room);
        System.out.println(room.getRoom_type());

        return ResponseEntity.status(200).body(200);
    }
}
