package com.MacrohardStudio.controller;

import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.rootTable.Room;
import com.MacrohardStudio.service.interfaces.IRoomModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mode")
public class RoomModeController {

    @Autowired
    private IRoomModeService iRoomModeService;

    @PostMapping(value = "/change")
    public ResponseCode change(@RequestBody Room room){ return iRoomModeService.change(room);}
}
