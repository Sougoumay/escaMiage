package m2.miage.interop.serviceactualite.service;

import m2.miage.interop.serviceactualite.dto.BadgeUtilisateurDTO;
import m2.miage.interop.serviceactualite.dto.UtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.TemplateInexistantEXception;
import m2.miage.interop.serviceactualite.service.facade.FacadeActualiteAutomatique;
import m2.miage.interop.serviceactualite.service.facade.FacadeGestionHistorique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private final FacadeGestionHistorique facadeGestionHistorique;
    private final FacadeActualiteAutomatique facadeActualiteAutomatique;

    public RabbitMqReceiver(FacadeGestionHistorique facadeGestionHistorique, FacadeActualiteAutomatique facadeActualiteAutomatique) {
        this.facadeGestionHistorique = facadeGestionHistorique;
        this.facadeActualiteAutomatique = facadeActualiteAutomatique;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}

    @RabbitListener(queues = "utilisateur.suppression.queue.actualite")
    public void supprimerUnUtilisateur(UtilisateurDTO utilisateur) {
        logger.info("On supprime cet utilisateur{}", utilisateur);
        facadeGestionHistorique.supprimerUtilsateur(utilisateur);

    }

    @RabbitListener(queues = "utilisateur.ajout.queue.actualite")
    public void recevoirUnUtilisateur(UtilisateurDTO utilisateur) {
        logger.info("Un nouvel utilisateur{}", utilisateur);
        facadeGestionHistorique.ajouterUtilisateur(utilisateur);
    }

    @RabbitListener(queues = {"badge.queue.actualite"})
    public void recevoirUnBadge(List<BadgeUtilisateurDTO> badgesUtilisateur) {
        logger.info("Les nouveaux badges{}", badgesUtilisateur);
        try {
            this.facadeActualiteAutomatique.genererPostBadge(badgesUtilisateur);
        } catch (TemplateInexistantEXception e) {
            logger.error("Aucun template disponible : {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "classement.queue.actualite")
    public void recevoirUnClassement(List<UtilisateurDTO> utilisateursDTO) {
        logger.info("Un nouveau classement");
        this.facadeActualiteAutomatique.genererPostClassement(utilisateursDTO);
    }



}