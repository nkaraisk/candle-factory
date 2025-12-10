package gr.ckaraiskos.candlefactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CandleFactoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandleFactoryApplication.class, args);
    }

}
