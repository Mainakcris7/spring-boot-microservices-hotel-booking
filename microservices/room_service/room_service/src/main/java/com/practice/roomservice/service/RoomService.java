package com.practice.roomservice.service;

import com.practice.roomservice.apiclient.BookingApiClient;
import com.practice.roomservice.apiclient.HotelApiClient;
import com.practice.roomservice.dto.RoomSaveDto;
import com.practice.roomservice.dto.RoomUpdateDto;
import com.practice.roomservice.exception.RoomException;
import com.practice.roomservice.model.Room;
import com.practice.roomservice.repository.RoomRepository;
import com.practice.roomservice.utils.RoomMapperUtil;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RoomService {
    private final HotelApiClient hotelApiClient;
    private final BookingApiClient bookingApiClient;
    private final RoomRepository repo;
    private final RoomMapperUtil mapper;

    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = repo.findAll();
        if(rooms.isEmpty()){
            log.error("No rooms found!");
            throw new RoomException("No rooms found", HttpStatus.NOT_FOUND);
        }

        log.info("All room details returned");
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    public ResponseEntity<Room> getRoomById(int id){
        Room room = repo.findById(id).orElse(null);

        if(room == null){
            log.error("No room found with id: {}", id);
            throw new RoomException(String.format("No room found with id: %d", id), HttpStatus.NOT_FOUND);
        }

        log.info("Room details for room with id: {} returned", id);
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }

    @Transactional
    public ResponseEntity<Room> saveRoom(RoomSaveDto dto){
        if(hotelApiClient.hotelExistsById(dto.getHotelId())){
            Room room = mapper.mapSaveDtoToRoom(dto);
            Room savedRoom = repo.save(room);

            log.info("New room saved with id: {}", savedRoom.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        }

        log.error("Saving 'Room' failed as no 'Hotel' found with id: {}", dto.getHotelId());
        throw new RoomException(String.format("Saving 'Room' failed as no 'Hotel' found with id: %d", dto.getHotelId()), HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public ResponseEntity<Room> updateRoom(RoomUpdateDto dto){
        Room toBeUpdated = repo.findById(dto.getId()).orElse(null);
        if(toBeUpdated == null){
            log.error("Update failed as no room found with id: {}", dto.getId());
            throw new RoomException(String.format("Update failed as no room found with id: %d", dto.getId()), HttpStatus.BAD_REQUEST);
        }
        Room room = mapper.mapUpdateDtoToRoom(dto, toBeUpdated);
        Room updatedRoom = repo.save(room);

        log.info("Room updated with id: {}", updatedRoom.getId());
        return ResponseEntity.ok(updatedRoom);
    }

    @Transactional
    public ResponseEntity<Void> deleteRoomById(int id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            // Deleting a room should not be stopped if it is not associated with any bookings
            try{
                bookingApiClient.deleteBookingsByRoomId(id);
            }catch(FeignException.FeignClientException e){
                log.warn(e.getMessage());
            }

            log.info("Room deleted with id: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        log.error("Deletion failed as no room found with id: {}", id);
        throw new RoomException(String.format("Deletion failed as no room found with id: %d", id), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<Void> deleteRoomsByHotelId(int id) {
        List<Room> rooms = repo.findByHotelId(id);

        if(rooms.isEmpty()){
            log.warn("No rooms found for hotel with id: {}", id);
        }

        rooms.forEach(room -> {
            repo.delete(room);
            log.info("Room deleted with id: {} for hotel with id: {}", room.getId(), id);
            try{
                bookingApiClient.deleteBookingsByRoomId(id);
            }catch(FeignException.FeignClientException e){
                log.warn(e.getMessage());
            }
        });
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
