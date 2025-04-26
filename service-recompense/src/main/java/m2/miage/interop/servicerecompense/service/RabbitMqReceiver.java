package m2.miage.interop.servicerecompense.service;

import m2.miage.interop.servicerecompense.dto.PartieDTO;
import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionAttributionBadge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private final FacadeGestionAttributionBadge facadeGestionAttributionBadge;

    public RabbitMqReceiver(FacadeGestionAttributionBadge facadeGestionAttributionBadge) {
        this.facadeGestionAttributionBadge = facadeGestionAttributionBadge;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}

    @RabbitListener(queues = "utilisateur.suppression.queue.recompense")
    public void supprimerUnUtilisateur(UtilisateurDTO utilisateur) {
        logger.info("On supprime cet utilsateur utilisateur{}", utilisateur);
        facadeGestionAttributionBadge.supprimerUtilisateur(utilisateur.getId());
    }

    @RabbitListener(queues = "utilisateur.ajout.queue.recompense")
    public void ajouterUnUtilisateur(UtilisateurDTO utilisateur) {
        logger.info("On ajoute cet utilsateur utilisateur{}", utilisateur);
        facadeGestionAttributionBadge.creerUtilisateur(utilisateur);
    }

    @RabbitListener(queues = "jeu.queue.recompense")
    public void recevoirUnePartie(PartieDTO statistiques) {
        logger.info("Une nouvelle partie{}", statistiques);
        facadeGestionAttributionBadge.creerPartie(statistiques); //On va lancer les traitements de badge
    }

}