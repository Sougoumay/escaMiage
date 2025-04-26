package m2.miage.interop.serviceactualite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    long idPost;

    String contenu;

    long utilisateurDTO;

    byte[] imagePost;

    List<ReactionUtilisateurDTO> reactionDTOList;

    public PostDTO(long idPost, String contenu, byte[] imagePost) {
        this.idPost = idPost;
        this.contenu = contenu;
        this.imagePost = imagePost;
    }

    public PostDTO(long idPost, String contenu, byte[] imagePost, List<ReactionUtilisateurDTO> reactionDTOList) {
        this.idPost = idPost;
        this.contenu = contenu;
        this.imagePost = imagePost;
        this.reactionDTOList = reactionDTOList;
    }

}
