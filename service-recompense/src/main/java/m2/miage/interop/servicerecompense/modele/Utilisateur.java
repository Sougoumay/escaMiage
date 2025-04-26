package m2.miage.interop.servicerecompense.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur {

    @Id
    private long id;

    private long meilleurTemps;

    private long meilleurScore;

    private LocalDate dateDernierePartie;

    private int nbPartiesJouees;

    private int scoreSemaine;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BadgeUtilisateur> badgeUtilisateurs;


    public Utilisateur() {
    }

    public Utilisateur(long id) {
        this.id = id;
        this.meilleurScore = 0;
        this.dateDernierePartie = null;
        this.nbPartiesJouees = 0;
        this.meilleurTemps = 0;
        this.scoreSemaine = 0;
    }

    public Utilisateur(long id, long meilleurScore, long meilleurTemps, LocalDate dateDernierePartie, int nbPartiesJouees, int scoreSemaine) {
        this.id = id;
        this.meilleurScore = meilleurScore;
        this.dateDernierePartie = dateDernierePartie;
        this.nbPartiesJouees = nbPartiesJouees;
        this.meilleurTemps = meilleurTemps;
        this.scoreSemaine = scoreSemaine;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", meilleurScore=" + meilleurScore +
                ", dateDernierePartie=" + dateDernierePartie +
                ", nbPartiesJouees=" + nbPartiesJouees +
                '}';
    }
}
