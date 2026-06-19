package ec.edu.espe.banquito.banquitoclearinghouseadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BanquitoClearinghouseAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanquitoClearinghouseAdapterApplication.class, args);
    }

}
