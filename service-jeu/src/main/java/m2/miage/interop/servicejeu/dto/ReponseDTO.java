package m2.miage.interop.servicejeu.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReponseDTO {

    // pour la création
    public ReponseDTO(long partieId, long id, String question, String difficulte, String indice) {
        this.id = id;
        this.partieId = partieId;
        this.question = question;
        this.difficulte = difficulte;
        this.nombreTentative = 0;
        this.indice = indice;
        this.score = 0;
        this.isRepondu = false;
    }

    public ReponseDTO(long id, long partieId, String question, boolean isRepondu) {
        this.id = id;
        this.partieId = partieId;
        this.question = question;
        this.isRepondu = isRepondu;
    }

    private long id;
    private long partieId;
    private String question;
    private String difficulte;
    private String indice;
    private int nombreTentative;
    private float score;
    private boolean isRepondu;
}

