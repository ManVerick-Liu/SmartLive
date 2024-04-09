package com.MacrohardStudio.service.interfaces;

import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.enums.Room_Mode;
import org.springframework.http.ResponseEntity;

public interface IRoomModeService {
    public ResponseEntity<Integer> change(Room room);


}
