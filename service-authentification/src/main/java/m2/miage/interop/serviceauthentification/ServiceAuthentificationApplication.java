package m2.miage.interop.serviceauthentification;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ServiceAuthentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthentificationApplication.class, args);
    }

}
