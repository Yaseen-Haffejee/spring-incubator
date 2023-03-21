package entelect.training.spring.booking.client;

import entelect.training.spring.booking.client.gen.CaptureRewardsRequest;
import entelect.training.spring.booking.client.gen.CaptureRewardsResponse;
import entelect.training.spring.booking.model.Customer;
import entelect.training.spring.booking.model.Flight;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;
import java.util.Map;

public class LoyaltyClient extends WebServiceGatewaySupport {

    public CaptureRewardsResponse updateLoyalty(Customer customerResponse, Flight flightResponse){
//        JsonParser springParser = JsonParserFactory.getJsonParser();
//        Map< String, Object > customerObj = springParser.parseMap(customerResponse);
//        Map< String, Object > flightObj = springParser.parseMap(flightResponse);
        String passportNumber = (String) customerResponse.getPassportNumber();
//        BigDecimal price = BigDecimal.valueOf((Double) flightObj.get("seatCost"));
        BigDecimal price = BigDecimal.valueOf(flightResponse.getSeatCost());
        System.out.println(price);
        System.out.println(passportNumber);

        CaptureRewardsRequest rewardsRequest = new CaptureRewardsRequest();
        rewardsRequest.setPassportNumber(passportNumber);
        rewardsRequest.setAmount(price);

        CaptureRewardsResponse rewardsResponse = (CaptureRewardsResponse) getWebServiceTemplate().
                                                    marshalSendAndReceive(rewardsRequest);


        return rewardsResponse;
    }
}
