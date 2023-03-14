package entelect.training.spring.booking.exceptions;


public class NotFoundException extends RuntimeException {

    public NotFoundException(String message, Throwable error){
        super(message,error);
    }
    public NotFoundException(String message){
        super(message);
    }
}
