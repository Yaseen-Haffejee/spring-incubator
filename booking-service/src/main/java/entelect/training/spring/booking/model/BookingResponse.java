package entelect.training.spring.booking.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Data
@Component
public class BookingResponse {

    private Integer customerId;
    private Integer flightId;
    private String reference;
    private LocalDate bookingDate;

    private Flight flightDetails;


    public BookingResponse fromEntity(Booking booking){
        BookingResponse response = new BookingResponse();
        response.customerId = booking.getCustomerId();
        response.flightId = booking.getFlightId();
        response.reference = booking.getReference();
        response.bookingDate = booking.getBookingDate();
        return  response;
    }
}
