package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import com.MacrohardStudio.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private IRoomService iRoomService;

    @PostMapping(value = "/add")
    public Room add(@RequestBody RoomDro roomDro){return iRoomService.add(roomDro);}

    @PostMapping(value = "/modify")
    public ResponseEntity<Integer> modify(@RequestBody Room room){return iRoomService.modify(room);}

}
