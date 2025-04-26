package m2.miage.interop.serviceauthentification.service;

import m2.miage.interop.serviceauthentification.dto.ModifierPassword;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@Service
public class RabbitMqSender {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("#{'${spring.rabbitmq.routingkey}'.split(',')}")
    private List<String> routingkey;

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);

    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void ajouterUtilisateur(UtilisateurDTO utilisateurDTO) {
        logger.info("ajouterUtilisateur");
        rabbitTemplate.convertAndSend(exchange,routingkey.getFirst(), utilisateurDTO);
    }

    public void modifierPassword(ModifierPassword modifierPassword) {
        logger.info("modifierPassword");
        rabbitTemplate.convertAndSend(exchange,routingkey.get(2), modifierPassword);
    }

    public void supprimerUtilisateur(UtilisateurDTO utilisateurDTO) {
        logger.info("supprimerUtilisateur");
        rabbitTemplate.convertAndSend(exchange,routingkey.get(1), utilisateurDTO);
    }


}
