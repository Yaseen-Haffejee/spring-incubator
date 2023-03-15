package entelect.training.spring.booking.client;

import entelect.training.spring.booking.client.gen.CaptureRewardsRequest;
import entelect.training.spring.booking.client.gen.CaptureRewardsResponse;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;
import java.util.Map;

public class LoyaltyClient extends WebServiceGatewaySupport {

    public CaptureRewardsResponse updateLoyalty(String customerResponse, String flightResponse){
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map< String, Object > customerObj = springParser.parseMap(customerResponse);
        Map< String, Object > flightObj = springParser.parseMap(flightResponse);
        String passportNumber = (String) customerObj.get("passportNumber");
        BigDecimal price = BigDecimal.valueOf((Double) flightObj.get("seatCost"));
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
