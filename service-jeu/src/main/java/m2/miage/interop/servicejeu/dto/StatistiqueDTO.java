package m2.miage.interop.servicejeu.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StatistiqueDTO {

    private long id;
    private long idUtilisateur;
    private long dureePartieEnSecondes; // Temps total de la partie
    private long tempsRestant; // Temps restant à la fin de la partie
    private int nbTentativesTotal;
    private double score; // Score obtenu durant la partie
    private int reponsesCorrectes; // Nombre de réponses correctes
    private boolean isReussie;
    private List<String> themes; // Thèmes abordés dans cette partie
}
