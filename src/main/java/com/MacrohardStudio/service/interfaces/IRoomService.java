package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import org.springframework.http.ResponseEntity;

public interface IRoomService {

    public Room add(RoomDro roomDro);

    public ResponseEntity<Integer> modify(Room room);
}
