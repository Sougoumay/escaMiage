package m2.miage.interop.servicerecompense.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicerecompense.modele.constraints.BadgeUtilisateurPK;

@Getter
@Setter
@Entity
public class BadgeUtilisateur {

    @EmbeddedId
    private BadgeUtilisateurPK id;

    @ManyToOne()
    @MapsId("idUtilisateur")
    private Utilisateur utilisateur;

    @ManyToOne()
    @MapsId("idBadge")
    private Badge badge;


    // CONSTRUCTORS

    public BadgeUtilisateur(Utilisateur utilisateur,Badge badge) {
        this.id = new BadgeUtilisateurPK(badge.getId(), utilisateur.getId());
        this.badge = badge;
        this.utilisateur = utilisateur;
    }

    public BadgeUtilisateur() {
    }


}
