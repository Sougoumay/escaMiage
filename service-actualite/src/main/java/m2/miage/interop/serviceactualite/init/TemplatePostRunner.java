package m2.miage.interop.serviceactualite.init;

import m2.miage.interop.serviceactualite.dao.TemplatePostRepository;
import m2.miage.interop.serviceactualite.modele.TemplatePost;
import m2.miage.interop.serviceactualite.modele.enums.TypeBadge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TemplatePostRunner implements CommandLineRunner {

    private final TemplatePostRepository templatePostRepository;
    private static final Logger logger = LoggerFactory.getLogger(TemplatePostRunner.class);

    public TemplatePostRunner(TemplatePostRepository templatePostRepository) {
        this.templatePostRepository = templatePostRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initTemplate();
    }

    public void initTemplate() {
        if (templatePostRepository.count() == 0) { // Vérifie si la table est vide avant d'insérer
            logger.info("Insertion des template dans la base de données...");

            List<TemplatePost> templatePosts = List.of(
                    new TemplatePost(TypeBadge.MEILLEUR_TEMPS.name(),
                            "{UTILISATEUR} a obtenu le badge {BADGE} avec un record de {VALEUR} secondes ! 🎉"),
                    new TemplatePost(TypeBadge.TEMPS_MOYEN.name(),
                            "{UTILISATEUR} a une moyenne de {VALEUR} secondes sur ses parties."),
                    new TemplatePost(TypeBadge.PIRE_TEMPS.name(),
                            "Aïe ! {UTILISATEUR} a eu le pire temps avec {VALEUR} secondes. 😅"),
                    new TemplatePost(TypeBadge.TENTATIVES.name(),
                            "{UTILISATEUR} a tenté {VALEUR} fois pour obtenir ce score ! 🔥"),
                    new TemplatePost(TypeBadge.THEME.name(),
                            "{UTILISATEUR} adore le thème {VALEUR} ! 🎨")
            );

            templatePostRepository.saveAll(templatePosts);

            logger.info("Les templates ont été insérés avec succés");
        } else {
            logger.warn("Les templates existent déjà, pas d'insertion nécessaire.");
        }
    }
}
