package m2.miage.interop.servicerecompense.modele.constraints;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicerecompense.modele.Badge;
import m2.miage.interop.servicerecompense.modele.Utilisateur;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BadgeUtilisateurPK implements Serializable {
    private long idUtilisateur;
    private long idBadge;

    public BadgeUtilisateurPK() {
    }

    public BadgeUtilisateurPK(long idUtilisateur, long idBadge) {
        this.idUtilisateur = idUtilisateur;
        this.idBadge = idBadge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeUtilisateurPK that = (BadgeUtilisateurPK) o;
        return idUtilisateur == that.idUtilisateur && idBadge == that.idBadge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtilisateur, idBadge);
    }
}
