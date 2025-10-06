package com.practice.roomservice.controller;

import com.practice.roomservice.dto.RoomSaveDto;
import com.practice.roomservice.dto.RoomUpdateDto;
import com.practice.roomservice.model.Room;
import com.practice.roomservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
    private final RoomService service;

    @GetMapping("")
    public ResponseEntity<List<Room>> getAllRooms(){
        return service.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable int id){
        return service.getRoomById(id);
    }

    @PostMapping("")
    public ResponseEntity<Room> saveRoom(@RequestBody @Valid RoomSaveDto dto){
        return service.saveRoom(dto);
    }

    @PutMapping("")
    public ResponseEntity<Room> updateRoom(@RequestBody @Valid RoomUpdateDto dto){
        return service.updateRoom(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable int id){
        return service.deleteRoomById(id);
    }

    @DeleteMapping("/hotel/{id}")
    public ResponseEntity<Void> deleteRoomsByHotelId(@PathVariable int id){
        return service.deleteRoomsByHotelId(id);
    }
}
