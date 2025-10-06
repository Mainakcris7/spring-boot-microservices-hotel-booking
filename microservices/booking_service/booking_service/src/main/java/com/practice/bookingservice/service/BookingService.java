package com.practice.bookingservice.service;

import com.mainak.dto.BookingDetails;
import com.mainak.dto.BookingType;
import com.practice.bookingservice.apiclient.HotelApiClient;
import com.practice.bookingservice.apiclient.RoomApiClient;
import com.practice.bookingservice.dto.*;
import com.practice.bookingservice.exception.BookingException;
import com.practice.bookingservice.model.Booking;
import com.practice.bookingservice.repository.BookingRepository;
import com.practice.bookingservice.utils.BookingMapperUtil;
import com.practice.bookingservice.utils.NotificationUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository repo;
    private final BookingMapperUtil mapperUtil;
    private final RoomApiClient roomApiClient;
    private final HotelApiClient hotelApiClient;
    private final NotificationService notificationService;

    public ResponseEntity<List<Booking>> getAllBookings(){
        List<Booking> bookings = repo.findAll();
        if(bookings.isEmpty()){
            log.error("No bookings found");
            throw new BookingException("No bookings found", HttpStatus.NOT_FOUND);
        }

        log.info("All booking details returned");
        return ResponseEntity.ok(bookings);
    }

    public ResponseEntity<Booking> getBookingById(int id){
        Booking booking = repo.findById(id).orElse(null);
        if(booking == null){
            log.error("No booking found with id: {}", id);
            throw new BookingException(String.format("No booking found with id: %d", id), HttpStatus.NOT_FOUND);
        }

        log.info("Booking details returned for booking with id: {}", id);
        return ResponseEntity.ok(booking);
    }

    @Transactional
    public ResponseEntity<BookingConfirmationDto> saveBooking(BookingSaveDto dto){
        Booking booking = mapperUtil.mapFromSaveDto(dto);
        RoomDto room = roomApiClient.getRoomById(booking.getRoomId());
        // Room with specified id not found
        if(room == null){
            log.error("Can't find room with id: {}", dto.getRoomId());
            throw new BookingException(String.format("Can't find room with id: %d", dto.getRoomId()), HttpStatus.BAD_REQUEST);
        }

        if(booking.getBookedUntil().isBefore(booking.getBookedFrom())){
            log.error("Booking end date: {} can't be before booking start date: {}", dto.getBookedUntil(), dto.getBookedFrom());
            throw new BookingException(String.format("Booking end date: %s can't be before booking start date: %s", dto.getBookedUntil(), dto.getBookedFrom()), HttpStatus.BAD_REQUEST);
        }

        // To check the hotelId and the roomId are compatible to each other (i.e. The room belongs to that hotel)
        if(room.getHotelId() != booking.getHotelId()){
            log.error("Can't find any room with id {} in hotel {}", dto.getRoomId(), dto.getHotelId());
            throw new BookingException(String.format("Can't find any room with id: %d in hotel with id: %d", dto.getRoomId(), dto.getHotelId()), HttpStatus.BAD_REQUEST);

        }

        // Logic to find out a room is not booked for overlapping dates.
        if(isOverlapping(booking)){
            log.error("Please select different date slot as booking slots are overlapping for room with id: {}", dto.getRoomId());
            throw new BookingException(String.format("Please select different date slot as booking slots are overlapping for room with id: %d", dto.getRoomId()), HttpStatus.BAD_REQUEST);
        }

        HotelDto hotelDto = hotelApiClient.getHotelById(dto.getHotelId());
        booking.setTotalPrice(calculateTotalPrice(dto.getBookedFrom(), dto.getBookedUntil(), room.getPricePerNight()));

        // Dummy customer data (To be replaced with actual with API call)
        CustomerDto customerDto = new CustomerDto(dto.getCustomerId(), "Mainak Mukherjee", "m@email.com");

        Booking savedBooking = repo.save(booking);
        BookingConfirmationDto bookingConfirmationDto = mapperUtil.mapConfirmationDtoFromBooking(savedBooking, hotelDto, room);

        log.info("New booking details saved with id: {}", savedBooking.getId());

        // Send notification
        BookingDetails bookingDetails = NotificationUtil.toBookingDetails(BookingType.CREATE, savedBooking, customerDto, hotelDto, room);
        notificationService.sendNotification("booking-create", bookingDetails);

        log.info("Event pushed for notification service to send notification for booking creation");
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingConfirmationDto);

    }

    @Transactional
    public ResponseEntity<BookingConfirmationDto> updateBooking(BookingUpdateDto dto){

        Booking booking = repo.findById(dto.getId()).orElse(null);
        // Booking with specified id not found
        if(booking == null){
            log.error("Update failed as booking with id: {} can't be found", dto.getId());
            throw new BookingException(String.format("Update failed as booking with id: %d can't be found", dto.getId()), HttpStatus.BAD_REQUEST);
        }

        Booking toBeUpdated = mapperUtil.mapFromUpdateDto(dto, booking);

        if(booking.getBookedUntil().isBefore(booking.getBookedFrom())){
            log.error("Booking end date: {} can't be before booking start date: {}", dto.getBookedUntil(), dto.getBookedFrom());
            throw new BookingException(String.format("Booking end date: %s can't be before booking start date: %s", dto.getBookedUntil(), dto.getBookedFrom()), HttpStatus.BAD_REQUEST);
        }

        // Logic to find out a room is not booked for overlapping dates.
        if(isOverlapping(toBeUpdated)){
            log.error("Please select different date slot, as booking slots are overlapping for room with id: {}", booking.getRoomId());
            throw new BookingException(String.format("Please select different date slot as booking slots are overlapping for room with id: %d", booking.getRoomId()), HttpStatus.BAD_REQUEST);
        }

        HotelDto hotelDto = hotelApiClient.getHotelById(toBeUpdated.getHotelId());
        RoomDto roomDto = roomApiClient.getRoomById(toBeUpdated.getRoomId());

        booking.setTotalPrice(calculateTotalPrice(toBeUpdated.getBookedFrom(), toBeUpdated.getBookedUntil(), roomDto.getPricePerNight()));

        Booking savedBooking = repo.save(booking);
        BookingConfirmationDto bookingConfirmationDto = mapperUtil.mapConfirmationDtoFromBooking(savedBooking, hotelDto, roomDto);

        log.info("Booking details updated for booking with id: {}", bookingConfirmationDto.getId());

        return ResponseEntity.status(HttpStatus.OK).body(bookingConfirmationDto);
    }

    @Transactional
    public ResponseEntity<Void> deleteBookingById(int id){
        Booking booking = repo.findById(id).orElse(null);

        if(booking == null){
            log.error("Delete booking failed as booking with id: {} can't be found", id);
            throw new BookingException(String.format("Delete failed as booking with id: %d can't be found", id), HttpStatus.BAD_REQUEST);
        }

        repo.delete(booking);
        log.info("Booking deleted with id: {}", booking.getId());

        RoomDto roomDto = roomApiClient.getRoomById(booking.getRoomId());
        HotelDto hotelDto = hotelApiClient.getHotelById(booking.getHotelId());

        // Dummy customer data (To be replaced with actual with API call)
        CustomerDto customerDto = new CustomerDto(booking.getCustomerId(), "Mainak Mukherjee", "m@email.com");

        // Send notification
        BookingDetails bookingDetails = NotificationUtil.toBookingDetails(BookingType.CANCEL, booking, customerDto, hotelDto, roomDto);
        notificationService.sendNotification("booking-cancel", bookingDetails);

        log.info("Event pushed for notification service to send notification for booking deletion");

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteBookingsByCustomerId(int customerId){
        List<Booking> bookings = repo.findByCustomerId(customerId);

        if(bookings.isEmpty()){
            log.warn("Delete bookings by customer id: {} failed as no bookings are associated with that customer.", customerId);
        }

        bookings.forEach(booking -> {
            repo.delete(booking);
            log.info("Booking deleted with id: {}", booking.getId());

            RoomDto roomDto = roomApiClient.getRoomById(booking.getRoomId());
            HotelDto hotelDto = hotelApiClient.getHotelById(booking.getHotelId());
            // Dummy customer data (To be replaced with actual after API call)
            CustomerDto customerDto = new CustomerDto(booking.getCustomerId(), "Mainak Mukherjee", "m@email.com");

            // Send notification
            BookingDetails bookingDetails = NotificationUtil.toBookingDetails(BookingType.CANCEL, booking, customerDto, hotelDto, roomDto);
            notificationService.sendNotification("booking-cancel", bookingDetails);

            log.info("Event pushed for notification service to send notification for booking deletion");
        });
        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<Void> deleteBookingsByRoomId(int id) {
        List<Booking> bookings = repo.findByRoomId(id);

        if(bookings.isEmpty()){
            log.warn("No bookings found for room with id: {}", id);;
        }

        bookings.forEach(booking -> {
            repo.delete(booking);
            log.info("Booking deleted with id: {}", booking.getId());

            RoomDto roomDto = roomApiClient.getRoomById(booking.getRoomId());
            HotelDto hotelDto = hotelApiClient.getHotelById(booking.getHotelId());
            // Dummy customer data (To be replaced with actual after API call)
            CustomerDto customerDto = new CustomerDto(booking.getCustomerId(), "Mainak Mukherjee", "m@email.com");

            // Send notification
            BookingDetails bookingDetails = NotificationUtil.toBookingDetails(BookingType.CANCEL, booking, customerDto, hotelDto, roomDto);
            notificationService.sendNotification("booking-cancel", bookingDetails);

            log.info("Event pushed for notification service to send notification for booking deletion");
        });
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Utils
    // Calculate total price according to the booking and no. of days
    private double calculateTotalPrice(LocalDate bookedFrom, LocalDate bookedUntil, double pricePerNight){
        long days = ChronoUnit.DAYS.between(bookedFrom, bookedUntil);
        return days * pricePerNight;
    }

    // For any room, to find out whether the booking slots are overlapping or not
    private boolean isOverlapping(Booking booking){
        List<Booking> bookingsForRoomId = repo.findByRoomId(booking.getRoomId());

        for(Booking b: bookingsForRoomId){
            if((b.getId() != booking.getId()) && (b.getBookedFrom().isBefore(booking.getBookedUntil())) && (booking.getBookedFrom().isBefore(b.getBookedUntil()))){
                return true;
            }
        }
        return false;
    }
}
