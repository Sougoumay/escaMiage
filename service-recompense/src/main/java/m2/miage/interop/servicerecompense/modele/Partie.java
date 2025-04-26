package m2.miage.interop.servicerecompense.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicerecompense.modele.enums.Theme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Partie {

   
    @Id
    private long id;
    private long idUtilisateur;
    private int dureePartieEnSecondes;
    private int nbTentativesTotal;
    private int score;
    private int reponsesCorrectes;
    private LocalDate datePartie;
    private boolean isReussie;
    @ElementCollection
    @Enumerated(EnumType.STRING) // Recommandé pour les enums
    @Column(name = "theme")
    private List<Theme> themes = new ArrayList<>();


    // CONSTRUCTEUR

    public Partie(long id,long idUtilisateur, int dureePartieEnSecondes,int nbTentativesTotal, int score, int reponsesCorrectes, List<Theme> themes, boolean isReussie, LocalDate datePartie) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.dureePartieEnSecondes = dureePartieEnSecondes;
        this.nbTentativesTotal = nbTentativesTotal;
        this.score = score;
        this.reponsesCorrectes = reponsesCorrectes;
        this.themes = themes;
        this.isReussie = isReussie;
        this.datePartie = datePartie;
    }

    public Partie() {
    }

}
