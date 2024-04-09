package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IRoomDao;
import com.MacrohardStudio.model.Home_Room;
import com.MacrohardStudio.model.Room;
import com.MacrohardStudio.model.dro.RoomDro;
import com.MacrohardStudio.model.enums.Room_Mode;
import com.MacrohardStudio.service.interfaces.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private IRoomDao iRoomDao;

    public Room add(RoomDro roomDro) {

        System.out.println("enter add home");
        System.out.println(roomDro.getRoom_name());
        Room room = new Room();
        room.setRoom_mode(Room_Mode.none);
        room.setRoom_type(roomDro.getRoom_type());
        room.setRoom_name(roomDro.getRoom_name());
        iRoomDao.add(room);
        System.out.println(roomDro.getRoom_id());


        //RoomDro result= iRoomDao.select(room.getRoom_id());
        //获取room_id,并将其设置为home_room 的room_id字段，插入到home_room中
        Home_Room home_room = new Home_Room();
        home_room.setRoom_id(room.getRoom_id());
        home_room.setHome_id(roomDro.getHome_id());
        iRoomDao.addHome_Room(home_room);
        //System.out.println(result.getRoom_id());
//        result.setRoom_id(result.getRoom_id());
//        result.setRoom_name(result.getRoom_name());
//        result.setRoom_type(result.getRoom_type());
//        result.setRoom_mode(result.getRoom_mode());

        System.out.println("room and home_room add successfully !");

        return room;

    }



    public ResponseEntity<Integer> modify(Room room) {

        System.out.println("enter modify home name");
        System.out.println(room.getRoom_name()+room.getRoom_id());
        Room oldroom = iRoomDao.select(room.getRoom_id());
        if(room.getRoom_name()!= oldroom.getRoom_name()&&room.getRoom_name()!=null)
        {
            oldroom.setRoom_name(room.getRoom_name());
        }

        iRoomDao.modify(oldroom);

        System.out.println("modify ok!");

        return ResponseEntity.status(200).body(200);
    }
}
