package m2.miage.interop.serviceactualite.service;

import m2.miage.interop.serviceactualite.dao.PostRepository;
import m2.miage.interop.serviceactualite.dao.TemplatePostRepository;
import m2.miage.interop.serviceactualite.dao.UtilisateurRepository;
import m2.miage.interop.serviceactualite.dto.BadgeUtilisateurDTO;
import m2.miage.interop.serviceactualite.dto.UtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.TemplateInexistantEXception;
import m2.miage.interop.serviceactualite.modele.Post;
import m2.miage.interop.serviceactualite.modele.TemplatePost;
import m2.miage.interop.serviceactualite.modele.Utilisateur;
import m2.miage.interop.serviceactualite.modele.enums.TypePost;
import m2.miage.interop.serviceactualite.service.facade.FacadeActualiteAutomatique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class FacadeActualiteAutomatiqueImpl implements FacadeActualiteAutomatique {

    public final PostRepository postRepository;
    public final TemplatePostRepository templatePostRepository;
    public final UtilisateurRepository utilisateurRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacadeActualiteAutomatiqueImpl.class);

    public FacadeActualiteAutomatiqueImpl(PostRepository postRepository, TemplatePostRepository templatePostRepository, UtilisateurRepository utilisateurRepository) {
        this.postRepository = postRepository;
        this.templatePostRepository = templatePostRepository;
        this.utilisateurRepository = utilisateurRepository;
    }


    @Override
    public void genererPostBadge(List<BadgeUtilisateurDTO> badgesUtilisateur) throws TemplateInexistantEXception {
        for (BadgeUtilisateurDTO badgeUtilisateur : badgesUtilisateur) {
            TemplatePost template =
                    templatePostRepository.findByTypeBadge(badgeUtilisateur.getBadge().getConditionType()).orElseThrow(TemplateInexistantEXception::new);
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(badgeUtilisateur.getIdUtilisateur());
            if (utilisateurOpt.isEmpty()) {
                logger.error("Utilisateur introuvable pour le badge {}", badgeUtilisateur.getBadge().getNom());
                return; // on s'arrete la
            }
            Post post = getPost(badgeUtilisateur, utilisateurOpt.get(), template);
            postRepository.save(post);
        }
    }

    private static Post getPost(BadgeUtilisateurDTO badgeUtilisateur, Utilisateur utilisateur, TemplatePost templateChoisi) {
        String valeur = String.valueOf(badgeUtilisateur.getBadge().getConditionValue()).replaceAll("[^0-9]", "");
        String contenu = templateChoisi.getContenuTemplate()
                .replace("{UTILISATEUR}", utilisateur.getPseudo()) // Remplace par le pseudo de l'utilisateur
                .replace("{BADGE}", badgeUtilisateur.getBadge().getNom())
                .replace("{VALEUR}",valeur );
        Post post = new Post();
        post.setUtilisateur(utilisateur);
        post.setType(TypePost.AUTO);
        post.setContenu(contenu);
        return post;
    }

    @Override
    public void genererPostClassement(List<UtilisateurDTO> utilisateursDTO) {
        StringBuilder contenuPost = new StringBuilder("Top 10 des utilisateurs cette semaine :\n\n");
        for (UtilisateurDTO utilisateurDTO : utilisateursDTO) {
            String pseudo = utilisateurRepository.findById(utilisateurDTO.getId())
                    .map(Utilisateur::getPseudo)
                    .orElse("Utilisateur inconnu");

            contenuPost.append("Pseudo : ").append(pseudo)
                    .append(" - Score : ").append(utilisateurDTO.getScoreSemaine())
                    .append("\n");
        }
        Post post = new Post();
        post.setType(TypePost.AUTO);
        post.setContenu(contenuPost.toString());  // Contenu du post
        postRepository.save(post);
    }
}
