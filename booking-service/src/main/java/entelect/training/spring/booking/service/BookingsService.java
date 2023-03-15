package entelect.training.spring.booking.service;

import entelect.training.spring.booking.client.LoyaltyClient;
import entelect.training.spring.booking.client.gen.CaptureRewardsResponse;
import entelect.training.spring.booking.exceptions.NotFoundException;
import entelect.training.spring.booking.model.Booking;
import entelect.training.spring.booking.model.BookingResponse;
import entelect.training.spring.booking.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private LoyaltyClient loyaltyClient;
    private int referenceLength = 10;
    public BookingsService(BookingsRepository bookingsRepository, ReferenceService referenceService, BookingResponse bookingResponse,
                           RestTemplate restTemplate, LoyaltyClient loyaltyClient){
        this.bookingsRepository = bookingsRepository;
        this.referenceService = referenceService;
        this.bookingResponse = bookingResponse;
        this.restTemplate = restTemplate;
        this.loyaltyClient = loyaltyClient;
    }

    public BookingResponse makeABooking(int customerId, int flightId){
        String customerBody;
        String flightBody;
        try{
            // Get the customer using the Id and check if it is valid
            String customerUrl = customersUrl+"/"+customerId;
            ResponseEntity<String> customerResponse = restTemplate.getForEntity(customerUrl,String.class);
            customerBody = customerResponse.getBody();
        }
        catch(Exception e){
            throw new NotFoundException(String.format("A customer with id %x does not exist",customerId));
        }

        try{
            String flightUrl = flightsUrl+"/"+flightId;
            ResponseEntity<String> flightResponse = restTemplate.getForEntity(flightUrl,String.class);
            flightBody = flightResponse.getBody();
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

        CaptureRewardsResponse Amount = loyaltyClient.updateLoyalty(customerBody,flightBody);
        System.out.println(Amount.getBalance());
        return response;
    }

    public BookingResponse getBookingById(int Id){

        Optional<Booking> result = bookingsRepository.findById(Id);

        if(result != null){
            Booking booking = result.get();
            BookingResponse response = bookingResponse.fromEntity(booking);
            return response;
        }
        else{
            throw new NotFoundException(String.format("A booking with id %x does not exist",Id));
        }
    }

    public List<BookingResponse> getAllCustomerBookings(int customerID){
        List<Booking> customerBooking = bookingsRepository.findByCustomerId(customerID);

        if(customerBooking != null){
            List<Booking> allBookings = customerBooking;
            List<BookingResponse> bookingResponses = allBookings.stream().map(booking -> bookingResponse.fromEntity(booking)).collect(Collectors.toList());
            return  bookingResponses;
        }
        else{
            throw new NotFoundException(String.format("A booking with id %x does not exist",customerID));
        }
    }

    public BookingResponse getBookingByReference(String reference){

        Booking customerBooking = bookingsRepository.findByReference(reference);

        if(customerBooking != null){
            Booking booking = customerBooking;
            BookingResponse bookingResult= bookingResponse.fromEntity(booking);
            return  bookingResult;
        }
        else{
            throw new NotFoundException(String.format("A booking with reference %s does not exist",reference));
        }
    }

}
