package m2.miage.interop.serviceactualite.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.serviceactualite.modele.enums.ReactionPost;

@Getter
@Setter
@Entity
public class ReactionUtilisateur {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private ReactionPost reactionPost;

    public ReactionUtilisateur() {
    }

    public ReactionUtilisateur(Utilisateur utilisateur, ReactionPost reactionPost, Post post) {
        this.utilisateur = utilisateur;
        this.reactionPost = reactionPost;
        this.post = post;
    }

}
