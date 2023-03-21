package entelect.training.spring.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Flight {
    

    private Integer id;
    
    private String flightNumber;

    private String origin;

    private String destination;
    
    private LocalDateTime departureTime;
    
    private LocalDateTime arrivalTime;
    
    private Integer seatsAvailable;
    
    private Float seatCost;

}
