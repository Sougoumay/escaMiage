package m2.miage.interop.servicerecompense.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UtilisateurDTO {
    @Id
    private long id;

    private long meilleurTemps;

    private long meilleurScore;

    private LocalDate dateDernierePartie;

    private int nbPartiesJouees;

    private int scoreSemaine;


    // CONSTRUCTORS
    public UtilisateurDTO(long id, long meilleurScore, long meilleurTemps, LocalDate dateDernierePartie, int nbPartiesJouees, int scoreSemaine) {
        this.id = id;
        this.meilleurTemps = meilleurTemps;
        this.meilleurScore = meilleurScore;
        this.dateDernierePartie = dateDernierePartie;
        this.nbPartiesJouees = nbPartiesJouees;
        this.scoreSemaine = scoreSemaine;
    }

    public UtilisateurDTO(long id, int scoreSemaine) {
        this.id = id;
        this.scoreSemaine = scoreSemaine;
    }

    public UtilisateurDTO() {
    }

}
