package entelect.training.spring.booking.service;

import entelect.training.spring.booking.model.Customer;
import entelect.training.spring.booking.model.Flight;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationQueueService {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final String queueName, final Customer customerMessage, final Flight flightMessage) {
        final String phoneNumber = "\"" + (String) customerMessage.getPhoneNumber()+"\"";

        final String flightNumber = (String) flightMessage.getFlightNumber();

        final String userName = customerMessage.getFirstName() + " " + customerMessage.getLastName();
        final String date = LocalDate.now().toString();

        final String message  = "\""+ String.format("Molo Air: Confirming flight %s booked for %s on %s.",flightNumber,userName,date)+ "\"";


        final String messageToSend = String.format("{ \"phoneNumber\":%s, \"message\":%s}",phoneNumber,message);
        jmsTemplate.send(queueName, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(messageToSend);
                return (Message) message;
            }
        });
    }
}
