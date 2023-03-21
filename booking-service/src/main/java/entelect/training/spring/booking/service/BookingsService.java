package entelect.training.spring.booking.service;

import entelect.training.spring.booking.client.LoyaltyClient;
import entelect.training.spring.booking.client.gen.CaptureRewardsResponse;
import entelect.training.spring.booking.exceptions.NotFoundException;
import entelect.training.spring.booking.model.Booking;
import entelect.training.spring.booking.model.BookingResponse;
import entelect.training.spring.booking.model.Customer;
import entelect.training.spring.booking.model.Flight;
import entelect.training.spring.booking.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    private NotificationQueueService notificationQueueService;
    private int referenceLength = 10;
    public BookingsService(BookingsRepository bookingsRepository, ReferenceService referenceService, BookingResponse bookingResponse,
                           RestTemplate restTemplate, LoyaltyClient loyaltyClient, NotificationQueueService notificationQueueService){
        this.bookingsRepository = bookingsRepository;
        this.referenceService = referenceService;
        this.bookingResponse = bookingResponse;
        this.restTemplate = restTemplate;
        this.loyaltyClient = loyaltyClient;
        this.notificationQueueService = notificationQueueService;
    }

    private Flight getFlight(int flightId){
        String flightUrl = flightsUrl+"/"+flightId;
        ResponseEntity<Flight> flightResponse = restTemplate.getForEntity(flightUrl,Flight.class);
        Flight flightBody = flightResponse.getBody();
        return flightBody;
    }

    private Customer getCustomer(int customerId){
        String customerUrl = customersUrl+"/"+customerId;
        ResponseEntity<Customer> customerResponse = restTemplate.getForEntity(customerUrl,Customer.class);
        Customer customerBody = customerResponse.getBody();
        return customerBody;
    }

    private Flight updateFlight(Flight flight){
        String flightUrl = flightsUrl;
        int currentAvailableSeats = flight.getSeatsAvailable();
        flight.setSeatsAvailable(currentAvailableSeats - 1);
        ResponseEntity<Flight> flightResponse = restTemplate.postForEntity(flightUrl,flight,Flight.class);
        Flight flightBody = flightResponse.getBody();
        return flightBody;
    }

    public BookingResponse makeABooking(int customerId, int flightId){
        Customer customerBody;
        Flight flightBody;
        try{
            customerBody = getCustomer(customerId);
        }
        catch(Exception e){
            throw new NotFoundException(String.format("A customer with id %x does not exist",customerId));
        }

        try{
            flightBody =  getFlight(flightId);
            flightBody = updateFlight(flightBody);
        }
        catch(Exception e){
            throw new NotFoundException(String.format("A flight with id %x does not exist",flightId));
        }
        String reference = referenceService.getAlphaNumericString(referenceLength);

        Booking bookingToMake = new Booking();
        bookingToMake.setCustomerId(customerId);
        bookingToMake.setFlightId(flightId);
        bookingToMake.setReference(reference);
        bookingToMake.setBookingDate(LocalDate.now());

        bookingsRepository.save(bookingToMake);
        BookingResponse response = bookingResponse.fromEntity(bookingToMake);
        response.setFlightDetails(flightBody);


        CaptureRewardsResponse Amount = loyaltyClient.updateLoyalty(customerBody,flightBody);
        notificationQueueService.sendMessage("inbound.queue",customerBody,flightBody);
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
            List<BookingResponse> bookingResponses = allBookings.stream().
                    map(booking -> bookingResponse.fromEntity(booking)).
                    collect(Collectors.toList());

            bookingResponses.stream().forEach(response -> response.setFlightDetails(getFlight(response.getFlightId())));
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
