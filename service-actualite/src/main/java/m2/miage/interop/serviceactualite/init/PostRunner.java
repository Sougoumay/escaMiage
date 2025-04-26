package m2.miage.interop.serviceactualite.init;

import m2.miage.interop.serviceactualite.dao.PostRepository;
import m2.miage.interop.serviceactualite.dao.UtilisateurRepository;
import m2.miage.interop.serviceactualite.modele.Post;
import m2.miage.interop.serviceactualite.modele.Utilisateur;
import m2.miage.interop.serviceactualite.modele.enums.TypePost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("!test")
@Component
public class PostRunner implements CommandLineRunner {

    public final PostRepository postRepository;
    public final UtilisateurRepository utilisateurRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostRunner.class);

    public PostRunner(PostRepository postRepository, UtilisateurRepository utilisateurRepository) {
        this.postRepository = postRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void run(String... args) {
        initPosts();
    }

    public void initPosts() {
        if (postRepository.count() == 0) { // Vérifie si la table est vide avant d'insérer
            logger.info("Insertion des posts dans la base de données...");

            Utilisateur admin = new Utilisateur();
            admin.setId(1000L);
            admin.setPseudo("AdminGame");
            utilisateurRepository.save(admin);

            // Création de 15 posts manuels
            List<Post> posts = List.of(
                    new Post("Bienvenue sur Escape MIAGE ! Préparez-vous à résoudre des énigmes !", admin, TypePost.MANUEL),
                    new Post("Nouvelle salle d'escape game disponible dès aujourd'hui !", admin, TypePost.MANUEL),
                    new Post("Classement mis à jour ! Qui est en tête cette semaine ?", admin, TypePost.MANUEL),
                    new Post("Astuce du jour : Observez bien chaque détail, rien n'est laissé au hasard.", admin, TypePost.MANUEL),
                    new Post("Tournoi spécial ce week-end ! Inscrivez-vous dès maintenant.", admin, TypePost.MANUEL),
                    new Post("Félicitations aux gagnants du dernier défi ! 🎉", admin, TypePost.MANUEL),
                    new Post("Une mise à jour arrive bientôt avec de nouvelles fonctionnalités !", admin, TypePost.MANUEL),
                    new Post("Partagez vos meilleurs moments sur Escape MIAGE avec nous !", admin, TypePost.MANUEL),
                    new Post("Saviez-vous que certaines énigmes ont plusieurs solutions ?", admin, TypePost.MANUEL),
                    new Post("Un indice caché se trouve peut-être sur cette page... 👀", admin, TypePost.MANUEL),
                    new Post("Nouveau badge disponible : réussirez-vous à le débloquer ?", admin, TypePost.MANUEL),
                    new Post("Le mode compétitif arrive bientôt, restez connectés !", admin, TypePost.MANUEL),
                    new Post("Le saviez-vous ? Le premier escape game a été créé en 2007 !", admin, TypePost.MANUEL),
                    new Post("Nous recrutons des bêta-testeurs pour nos prochaines énigmes !", admin, TypePost.MANUEL),
                    new Post("Bonne chance à tous pour les énigmes du mois ! Que le meilleur gagne !", admin, TypePost.MANUEL)
            );

            postRepository.saveAll(posts);

            logger.info("Les posts ont été insérés avec succés");
        } else {
            logger.warn("Les posts existent déjà, pas d'insertion nécessaire.");
        }
    }
}
