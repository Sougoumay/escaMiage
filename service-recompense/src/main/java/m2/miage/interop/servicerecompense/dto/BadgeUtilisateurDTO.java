package m2.miage.interop.servicerecompense.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeUtilisateurDTO {

    long id;

    long idUtilisateur;

    BadgeDTO badge;

    public BadgeUtilisateurDTO(long utilisateur, BadgeDTO badge) {
        this.idUtilisateur = utilisateur;
        this.badge = badge;
    }

    public BadgeUtilisateurDTO() {
    }
}
