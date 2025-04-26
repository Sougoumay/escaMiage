package m2.miage.interop.serviceactualite.service.facade;

import m2.miage.interop.serviceactualite.dto.PostDTO;
import m2.miage.interop.serviceactualite.dto.ReactionUtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.ContenuPostInvalideException;
import m2.miage.interop.serviceactualite.exceptions.PostInexistantException;
import m2.miage.interop.serviceactualite.exceptions.ReactionUtilisateurIncorrectException;
import m2.miage.interop.serviceactualite.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceactualite.modele.enums.ReactionPost;

import java.util.List;


public interface FacadeActualite {

    //1) Ajouter un post dans le feed - Pour le compte du site internet
    PostDTO ajouterPost(PostDTO postDTO, long idUtilisateur) throws ContenuPostInvalideException, UtilisateurInexistantException;

    //3) Reagir à un post
    void reagirAUnPost(long idPost, long idUtilisateur, ReactionUtilisateurDTO reactionDTO) throws ReactionUtilisateurIncorrectException, UtilisateurInexistantException, PostInexistantException;

    // Supprimer un post
    void supprimerUnPost(long idPost) throws PostInexistantException;

    // Modifier un post
    void modifierUnPost(long id, PostDTO postDTO) throws PostInexistantException;

    ReactionPost getReactionPost(String reaction) throws IllegalArgumentException;

    PostDTO recupererUnPost(long idPost) throws PostInexistantException;

    List<PostDTO> recupererMesPosts(long idUtilisateur);
}
