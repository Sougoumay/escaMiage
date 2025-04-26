package m2.miage.interop.servicerecompense.init;

import m2.miage.interop.servicerecompense.dao.BadgeRepository;
import m2.miage.interop.servicerecompense.modele.Badge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class BadgeRunner implements CommandLineRunner {

    public final BadgeRepository badgeRepository;
    private static final Logger logger = LoggerFactory.getLogger(BadgeRunner.class);

    public BadgeRunner(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @Override
    public void run(String... args) {
        initBddForTestingRecompense();
    }

    private void initBddForTestingRecompense() {
        badgeRepository.deleteAll();
        if (badgeRepository.count() == 0) {
            //Insertion BADGE
            badgeRepository.save(new Badge("Flash MIAGE", new byte[0], "MEILLEUR_TEMPS", "<101"));
            badgeRepository.save(new Badge("Sans pression (Cool Coder)", new byte[0], "TENTATIVES", "<1500"));
            logger.info("Données insérées avec succès !");
        } else {
            logger.info("Les badges existent déjà, pas d'insertion nécessaire.");
        }
    }
}
