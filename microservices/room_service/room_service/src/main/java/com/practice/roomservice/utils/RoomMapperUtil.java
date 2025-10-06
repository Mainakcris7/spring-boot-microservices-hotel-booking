package com.practice.roomservice.utils;

import com.practice.roomservice.dto.RoomSaveDto;
import com.practice.roomservice.dto.RoomUpdateDto;
import com.practice.roomservice.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapperUtil {
    public Room mapSaveDtoToRoom(RoomSaveDto dto){
        Room room = new Room();
        room.setId(0);
        room.setHotelId(dto.getHotelId());
        room.setRoomType(dto.getRoomType());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCheckInTime(dto.getCheckInTime());
        room.setCheckOutTime(dto.getCheckOutTime());
        room.setAmenities(dto.getAmenities());

        return room;
    }

    public Room mapUpdateDtoToRoom(RoomUpdateDto dto, Room room){
        room.setId(dto.getId());
        room.setRoomType(dto.getRoomType());
        room.setPricePerNight(dto.getPricePerNight());
        room.setCheckInTime(dto.getCheckInTime());
        room.setCheckOutTime(dto.getCheckOutTime());
        room.setAmenities(dto.getAmenities());

        return room;
    }
}
