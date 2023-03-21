package entelect.training.spring.booking.controller;

import entelect.training.spring.booking.exceptions.NotFoundException;
import entelect.training.spring.booking.model.BookingResponse;
import entelect.training.spring.booking.model.BookingSearch;
import entelect.training.spring.booking.model.CreateBooking;
import entelect.training.spring.booking.service.BookingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
@CrossOrigin(origins ={"http://localhost:4200"})
public class BookingsController {
    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @PostMapping
    @CrossOrigin(origins ={"http://localhost:4200"})
    public ResponseEntity makeBooking(@RequestBody CreateBooking booking) {
        LOGGER.info("Creating a booking");
        try {
            int customerId = booking.getCustomerId();
            int flightId = booking.getFlightId();
            BookingResponse newBooking = bookingsService.makeABooking(customerId, flightId);
            LOGGER.info("Successfully created new booking");
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        } catch (NotFoundException ex) {
            LOGGER.error("Booking creation failed");
            LOGGER.error(ex.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("{id}")
    @CrossOrigin(origins ={"http://localhost:4200"})
    public ResponseEntity getBookingById(@PathVariable Integer id) {
        LOGGER.info("Looking for booking with Id {}", id);
        try {
            BookingResponse result = bookingsService.getBookingById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException ex) {
            LOGGER.error(ex.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/search")
    @CrossOrigin(origins ={"http://localhost:4200"})
    public ResponseEntity getBookings(@RequestBody BookingSearch search) {
        //search by reference string
        if (search.getReference() != null) {
            String reference = search.getReference();
            LOGGER.info("Looking for booking with reference {}", reference);
            try {
                BookingResponse result = bookingsService.getBookingByReference(reference);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } catch (NotFoundException ex) {
                LOGGER.error(ex.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
        }
        // search by customer id
        else {
            int customerId = search.getCustomerId();
            LOGGER.info("Looking for customer bookings with Customer Id {}", customerId);
            try {
                List<BookingResponse> result = bookingsService.getAllCustomerBookings(customerId);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } catch (NotFoundException ex) {
                LOGGER.error(ex.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
        }
    }
}

