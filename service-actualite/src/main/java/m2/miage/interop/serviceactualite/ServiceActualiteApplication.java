package m2.miage.interop.serviceactualite;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@SpringBootApplication
@EnableScheduling
public class ServiceActualiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceActualiteApplication.class, args);
    }

}
