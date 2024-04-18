package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.dto.ResponseCode;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.model.rootTable.Room;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IRoomModeService
{
    public ResponseCode change(Room room);

    public void roomModeHandler();

    public void purificationModeHandler(Integer room_id);
    public void sleepModeHandler(Integer room_id);
    public void studyModeHandler(Integer room_id);
    public void entertainmentModeHandler(Integer room_id);
    public void noneModeHandler(Integer room_id);
    public void securityModeHandler(Integer room_id);
    public void mealModeHandler(Integer room_id);
}
