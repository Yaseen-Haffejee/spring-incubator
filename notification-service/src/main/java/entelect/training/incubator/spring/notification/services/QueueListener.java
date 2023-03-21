package entelect.training.incubator.spring.notification.services;

import entelect.training.incubator.spring.notification.sms.client.impl.MoloCellSmsClient;
import org.apache.activemq.Message;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Map;


@Component
public class QueueListener {

    private MoloCellSmsClient smsClient;

    public QueueListener(MoloCellSmsClient smsClient){
        this.smsClient = smsClient;
    }


    @JmsListener(destination = "inbound.queue")
    @SendTo("outbound.queue")
    public String receiveMessage(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        String response = "Hello World!";

        JsonParser springParser = JsonParserFactory.getJsonParser();

        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            System.out.println("Message data is: "+messageData);
            response = messageData;
            Map< String, Object > responseMap = springParser.parseMap(messageData);
            final String phoneNumber = (String) responseMap.get("phoneNumber");
            final String message  = (String) responseMap.get("message");
            smsClient.sendSms(phoneNumber,message);

        }
        return response;
    }

}
