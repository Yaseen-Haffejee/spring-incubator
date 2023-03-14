package entelect.training.spring.booking.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Component
public class CreateBooking {

    private Integer customerId;
    private Integer flightId;
}
