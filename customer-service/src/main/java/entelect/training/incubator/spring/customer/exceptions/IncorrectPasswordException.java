package entelect.training.incubator.spring.customer.exceptions;

public class IncorrectPasswordException extends RuntimeException{

    public IncorrectPasswordException (String message){
        super(message);
    }

    public IncorrectPasswordException (String message, Throwable cause){
        super(message,cause);
    }
}
