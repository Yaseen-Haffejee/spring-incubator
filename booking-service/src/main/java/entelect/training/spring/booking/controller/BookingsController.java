package entelect.training.spring.booking.controller;

import entelect.training.spring.booking.exceptions.NotFoundException;
import entelect.training.spring.booking.model.BookingResponse;
import entelect.training.spring.booking.model.CreateBooking;
import entelect.training.spring.booking.service.BookingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
public class BookingsController {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private BookingsService bookingsService;
    public BookingsController(BookingsService bookingsService){
        this.bookingsService = bookingsService;
    }

    @PostMapping
    public ResponseEntity makeBooking(@RequestBody CreateBooking booking) {
        LOGGER.info("Creating a booking");
        try{
            int customerId = booking.getCustomerId();
            int flightId = booking.getFlightId();
            BookingResponse newBooking = bookingsService.makeABooking(customerId,flightId);
            LOGGER.info("Successfully created new booking");
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
        catch (NotFoundException ex){
            LOGGER.error("Booking creation failed");
            LOGGER.error(ex.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

//    @GetMapping("{id}")
//    public ResponseEntity getBookingById(@PathVariable Integer id){
//
//    }
}
