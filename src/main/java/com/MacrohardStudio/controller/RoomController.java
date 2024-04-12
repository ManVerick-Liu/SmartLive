package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.rootTable.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.dto.ResponseData;
import com.MacrohardStudio.model.enums.Room_Type;
import com.MacrohardStudio.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private IRoomService iRoomService;

    @PostMapping(value = "/add")
    public ResponseData<Room> add(@RequestBody RoomDro roomDro){return iRoomService.add(roomDro);}

    @PostMapping(value = "/modify")
    public ResponseCode modify(@RequestBody Room room){return iRoomService.modify(room);}

    @GetMapping(value = "/search")
    public ResponseData<List<Room>> search
            (@RequestParam(required = false)Integer room_id,
             @RequestParam(required = false)Room_Type room_type,
             @RequestParam(required = false)String room_name,
             @RequestParam(required = false)Integer home_id,
             @RequestParam(required = false)Integer device_id)
    {return iRoomService.search(room_id, room_type, room_name, home_id, device_id);}

}
