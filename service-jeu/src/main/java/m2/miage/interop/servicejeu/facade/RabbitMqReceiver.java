package m2.miage.interop.servicejeu.facade;

import m2.miage.interop.servicejeu.dto.UtilisateurDTO;
import m2.miage.interop.servicejeu.facade.interfaces.FacadePartie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    public final FacadePartie facadePartie;

    public RabbitMqReceiver(FacadePartie facadePartie) {
        this.facadePartie = facadePartie;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}

    @RabbitListener(queues = "utilisateur.suppression.queue.jeu")
    public void supprimerUnUtilisateur(UtilisateurDTO utilisateur) {
        logger.info("On doit supprimer cet utilisateur{}", utilisateur);
        facadePartie.supprimerUtilisateur(utilisateur);
    }

}