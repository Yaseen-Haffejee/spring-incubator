package entelect.training.spring.booking.service;

import entelect.training.spring.booking.exceptions.NotFoundException;
import entelect.training.spring.booking.model.Booking;
import entelect.training.spring.booking.model.BookingResponse;
import entelect.training.spring.booking.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingsService {

    @Value("${spring.api.host.customersUrl}")
    private String customersUrl;

    @Value("${spring.api.host.flightsUrl}")
    private String flightsUrl;
    private BookingsRepository bookingsRepository;
    private ReferenceService referenceService;

    private BookingResponse bookingResponse;

    private RestTemplate restTemplate;

    private int referenceLength = 10;
    public BookingsService(BookingsRepository bookingsRepository, ReferenceService referenceService, BookingResponse bookingResponse, RestTemplate restTemplate){
        this.bookingsRepository = bookingsRepository;
        this.referenceService = referenceService;
        this.bookingResponse = bookingResponse;
        this.restTemplate = restTemplate;
    }

    public BookingResponse makeABooking(int customerId, int flightId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        try{
            // Get the customer using the Id and check if it is valid
            String customerUrl = customersUrl+"/"+customerId;
            ResponseEntity<String> customerResponse = restTemplate.getForEntity(customerUrl,String.class);
        }
        catch(Exception e){
            throw new NotFoundException(String.format("A customer with id %x does not exist",customerId));
        }

        try{
            String flightUrl = flightsUrl+"/"+flightId;
            ResponseEntity<String> flightResponse = restTemplate.exchange(flightUrl, HttpMethod.GET,entity,String.class);
        }
        catch(Exception e){
            throw new NotFoundException(String.format("A flight with id %x does not exist",flightId));
        }
        String reference = referenceService.getAlphaNumericString(referenceLength);

        Booking bookingToMake = new Booking();
        bookingToMake.setCustomerId(customerId);
        bookingToMake.setFlightId(flightId);
        bookingToMake.setReference(reference);

        bookingsRepository.save(bookingToMake);
        BookingResponse response = bookingResponse.fromEntity(bookingToMake);
        return response;
    }
}
