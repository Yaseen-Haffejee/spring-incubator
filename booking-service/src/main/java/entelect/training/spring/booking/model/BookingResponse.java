package entelect.training.spring.booking.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Component
public class BookingResponse {

    private Integer customerId;
    private Integer flightId;
    private String reference;


    public BookingResponse fromEntity(Booking booking){
        BookingResponse response = new BookingResponse();
        response.customerId = booking.getCustomerId();
        response.flightId = booking.getFlightId();
        response.reference = booking.getReference();
        return  response;
    }
}
