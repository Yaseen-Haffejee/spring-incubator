package entelect.training.spring.booking;

import entelect.training.spring.booking.client.gen.CaptureRewardsResponse;
import entelect.training.spring.booking.config.ApplicationConfiguration;
import entelect.training.spring.booking.client.LoyaltyClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class LoyaltyServiceTest {

    @Autowired
    LoyaltyClient loyaltyService;

    @Test
    public void test(){
        CaptureRewardsResponse response = loyaltyService.updateLoyalty("123445644", String.valueOf(BigDecimal.valueOf(225.0)));
    }
}
