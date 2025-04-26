package m2.miage.interop.servicerecompense.service;

import m2.miage.interop.servicerecompense.dto.BadgeUtilisateurDTO;
import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMqSender {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("#{'${spring.rabbitmq.routingkey}'.split(',')}")
    private List<String> routingKeys;

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void envoyerBadgeUtilisateur(List<BadgeUtilisateurDTO> listeNouveauxBadges){
        logger.info("Badge Utilisateur envoyé!");
        rabbitTemplate.convertAndSend(exchange,routingKeys.get(2), listeNouveauxBadges);
    }

    public void envoyerScoreSemaine(List<UtilisateurDTO> utilisateur){
        logger.info("Score semaine envoyé!");
        rabbitTemplate.convertAndSend(exchange, routingKeys.get(3), utilisateur);
    }
}
