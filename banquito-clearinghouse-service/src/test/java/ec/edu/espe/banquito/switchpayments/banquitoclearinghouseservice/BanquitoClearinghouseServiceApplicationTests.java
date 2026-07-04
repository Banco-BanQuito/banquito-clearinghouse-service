package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.mongodb.uri=mongodb://localhost:27017/clearingdb"
})
class BanquitoClearinghouseServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
