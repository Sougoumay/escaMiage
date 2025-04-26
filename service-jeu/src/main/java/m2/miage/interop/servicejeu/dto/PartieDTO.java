package m2.miage.interop.servicejeu.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartieDTO {

    private long id;

    private long idUtilisateur;

    private String etat;

    private double scoreFinal;

    private String code;

    private int nbreTentativeCode;

    private int indiceCode;

    private List<ReponseDTO> reponses;

    private LocalDateTime dateCreation;

    public PartieDTO(long id, long idUtilisateur, String etat, double scoreFinal, List<ReponseDTO> reponses,LocalDateTime dateCreation) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.etat = etat;
        this.reponses = reponses;
        this.scoreFinal = scoreFinal;
        this.dateCreation = dateCreation;
    }

    public PartieDTO(long id, long idUtilisateur, String etat, List<ReponseDTO> reponses, LocalDateTime dateCreation) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.etat = etat;
        this.reponses = reponses;
        this.dateCreation = dateCreation;
    }
}
