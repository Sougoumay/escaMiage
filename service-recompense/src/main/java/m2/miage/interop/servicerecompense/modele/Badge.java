package m2.miage.interop.servicerecompense.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
public class Badge {

    @Id
    @GeneratedValue
    long id;

    String nom;

    @Lob  // Création de BLOB ds BDD
    @Column(columnDefinition = "LONGBLOB")
    byte[] icone;

    //@ValidEnum(enumClass = TypeBadge.class, message = "Invalid badge type")
    String conditionType;

    String conditionValue;

    LocalDate dateCreation;

    LocalDate dateModification;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BadgeUtilisateur> badgeUtilisateurs;

    public Badge() {
    }

    public Badge(String nom, byte[] icone, String conditionType, String conditionValue) {
        this.nom = nom;
        this.icone = icone;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.dateCreation = LocalDate.now();
        this.dateModification = LocalDate.now();
    }

    public Badge(long id, String nom, byte[] icone, String conditionType, String conditionValue, LocalDate dateModification) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.dateModification = dateModification;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", icone=" + Arrays.toString(icone) +
                ", condition_type='" + conditionType + '\'' +
                ", condition_value='" + conditionValue + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                '}';
    }
}
