package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.model.rootTable.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IRoomModeService
{
    public ResponseCode change(Room room);
    public ResponseCode purificationModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode sleepModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode studyModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode entertainmentModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode noneModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode securityModeHandler(ResponseCode responseCode, Integer room_id);
    public ResponseCode mealModeHandler(ResponseCode responseCode, Integer room_id);
}
