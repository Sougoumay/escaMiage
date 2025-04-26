package m2.miage.interop.servicerecompense.scheduler;

import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionAttributionBadge;
import m2.miage.interop.servicerecompense.service.RabbitMqSender;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionClassement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecompenseListener {

    private final FacadeGestionClassement facadeGestionClassement;
    private final RabbitMqSender rabbitMqSender;
    private static final Logger logger = LoggerFactory.getLogger(RecompenseListener.class);

    public RecompenseListener(FacadeGestionClassement facadeGestionClassement, RabbitMqSender rabbitMqSender) {
        this.facadeGestionClassement = facadeGestionClassement;
        this.rabbitMqSender = rabbitMqSender;
    }

    @Scheduled(cron = "0 30 12 * * MON") // Exécution tous les lundis à 12h00
    //@Scheduled(fixedRate = 60000) // TEST - toutes les minutes
    public void reinitialisationClassementHebdomadaire() {
        facadeGestionClassement.resetScoresHebdomadaires(); // Réinitialise les scores
        logger.info("Classement hebdomadaire réinitialisé !");
    }

    @Scheduled(cron = "0 0 12 * * MON") // Exécution tous les lundis à 12h00
    //@Scheduled(fixedRate = 60000) // TEST - toutes les minutes
    public void envoiClassementHebdomadaire() {
        List<UtilisateurDTO> classHebdo = facadeGestionClassement.recupererClassementHebdo(10);
        rabbitMqSender.envoyerScoreSemaine(classHebdo);
        logger.info("Classement hebdomadaire envoyé !");
    }




}