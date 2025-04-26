package m2.miage.interop.servicerecompense.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicerecompense.modele.enums.Theme;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Statistiques {

    @GeneratedValue
    @Id
    private long id;
    private long idUtilisateur;
    private int nbTentativesGlobal;
    private int nbPartiesJouees;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private List<Theme> themes = new ArrayList<>();

    private int dureeMoyennePartie;
    private int meilleurTemps;
    private int pireTemps;
    private int scoreSemaine;

    // CONSTRUCTORS


    public Statistiques(long idUtilisateur, int nbTentativesGlobal, int nbPartiesJouees, int dureeMoyennePartie, int meilleurTemps, int pireTemps, List<Theme> themes, int scoreSemaine) {
        this.idUtilisateur = idUtilisateur;
        this.nbTentativesGlobal = nbTentativesGlobal;
        this.nbPartiesJouees = nbPartiesJouees;
        this.dureeMoyennePartie = dureeMoyennePartie;
        this.meilleurTemps = meilleurTemps;
        this.pireTemps = pireTemps;
        this.themes = themes;
        this.scoreSemaine = scoreSemaine;
    }

    public Statistiques() {
    }

    // GET SET


}
