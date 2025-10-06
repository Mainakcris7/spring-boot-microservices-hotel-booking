package com.practice.hotelservice.service;

import com.practice.hotelservice.apiclient.RoomApiClient;
import com.practice.hotelservice.dto.HotelSaveDto;
import com.practice.hotelservice.dto.HotelUpdateDto;
import com.practice.hotelservice.exception.HotelException;
import com.practice.hotelservice.model.Hotel;
import com.practice.hotelservice.repository.HotelRepository;
import com.practice.hotelservice.utils.HotelMapperUtil;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HotelService {
    private final HotelMapperUtil util;
    private final HotelRepository repo;
    private final RoomApiClient roomApiClient;

    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = repo.findAll();
        if(hotels.isEmpty()){
            log.error("No hotels found!");
            throw new HotelException("No hotels found!", HttpStatus.NOT_FOUND);
        }
        log.info("All hotel details returned successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    public ResponseEntity<Hotel> getHotelById(int id){
        Hotel hotel = repo.findById(id).orElse(null);
        if(hotel == null){
            log.error("No hotel found with id: {}", id);
            throw new HotelException(String.format("No hotel found with id: %d", id), HttpStatus.NOT_FOUND);
        }
        log.info("Hotel details returned successfully for hotel with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }

    public Boolean hotelExistsById(int id){
        log.info("Checking whether hotel exists with id: {}", id);
        return repo.existsById(id);
    }

    public ResponseEntity<Hotel> saveHotel(HotelSaveDto dto){
        Hotel hotel = util.mapSaveDtoToHotel(dto);
        Hotel savedHotel = repo.save(hotel);

        log.info("New hotel saved with id: {}", savedHotel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHotel);
    }

    public ResponseEntity<Hotel> updateHotel(HotelUpdateDto dto){
        Hotel hotel = util.mapUpdateDtoToHotel(dto);
        Hotel toBeUpdated = repo.findById(hotel.getId()).orElse(null);

        if(toBeUpdated == null){
            log.error("Failed to update as no hotel found with id: {}", dto.getId());
            throw new HotelException(String.format("Failed to update as no hotel found with id: %d", dto.getId()), HttpStatus.BAD_REQUEST);
        }

        hotel.setCreatedAt(toBeUpdated.getCreatedAt());
        Hotel updatedHotel = repo.save(hotel);

        log.info("Hotel details updated with id: {}", updatedHotel.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedHotel);
    }

    public ResponseEntity<Void> deleteHotelById(int id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            try{
                roomApiClient.deleteRoomsByHotelId(id);
            }catch (FeignException.FeignClientException e){
                log.warn(e.getMessage());
            }
            log.info("Hotel deleted with id: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        log.error("Failed to delete as no hotel found with id: {}", id);
        throw new HotelException(String.format("Failed to delete as no hotel found with id: %d", id), HttpStatus.BAD_REQUEST);
    }
}
