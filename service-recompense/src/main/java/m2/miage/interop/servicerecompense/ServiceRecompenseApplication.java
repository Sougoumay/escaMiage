package m2.miage.interop.servicerecompense;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRabbit
@EnableScheduling
public class ServiceRecompenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRecompenseApplication.class, args);
    }

}
