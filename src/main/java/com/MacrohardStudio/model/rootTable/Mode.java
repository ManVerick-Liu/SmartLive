package com.MacrohardStudio.model.rootTable;

import com.MacrohardStudio.model.enums.Room_Type;

public class Mode {
    private Integer room_id;
    private Room_Type room_mode;

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    public Room_Type getMode() {
        return room_mode;
    }

    public void setMode(Room_Type mode) {
        this.room_mode = room_mode;
    }
}
