package m2.miage.interop.serviceactualite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionUtilisateurDTO {

    String typeReaction;

    long idUtilisateur;

    PostDTO postDTO;

    public ReactionUtilisateurDTO(String typeReaction) {
        this.typeReaction = typeReaction;
    }

    public ReactionUtilisateurDTO(String typeReaction, long idUtilisateur, PostDTO postDTO) {
        this.typeReaction = typeReaction;
        this.idUtilisateur = idUtilisateur;
        this.postDTO = postDTO;
    }

    public ReactionUtilisateurDTO(String typeReaction, long idUtilisateur) {
        this.typeReaction = typeReaction;
        this.idUtilisateur = idUtilisateur;
    }

    public ReactionUtilisateurDTO() {
    }
}
