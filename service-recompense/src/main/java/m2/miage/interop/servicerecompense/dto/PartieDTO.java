package m2.miage.interop.servicerecompense.dto;

import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicerecompense.modele.enums.Theme;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PartieDTO {

    private long id;
    private long idUtilisateur;
    private int dureePartieEnSecondes;
    private int nbTentativesTotal;
    private int score;
    private int reponsesCorrectes;
    private boolean isReussie;
    private LocalDate datePartie;
    private List<String> themes;

    //CONSTRUCTORS

    public PartieDTO(long id, long idUtilisateur, int dureePartieEnSecondes,int nbTentativesTotal, int score, int reponsesCorrectes, boolean isReussie, List<String> themes, LocalDate datePartie) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.dureePartieEnSecondes = dureePartieEnSecondes;
        this.nbTentativesTotal = nbTentativesTotal;
        this.score = score;
        this.reponsesCorrectes = reponsesCorrectes;
        this.isReussie = isReussie;
        this.themes = themes;
        this.datePartie = datePartie;
    }

    public PartieDTO() {
    }

}
