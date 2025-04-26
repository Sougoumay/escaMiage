package m2.miage.interop.servicejeu.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnigmeDTO {

    public long id;
    public String question;
    public String reponse;
    public String difficulte;
    public String indice;
    public String theme;

    public EnigmeDTO(long id, String question, String reponse, String difficulte, String indice,String theme) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;
        this.difficulte = difficulte;
        this.indice = indice;
        this.theme = theme;
    }

    public EnigmeDTO() {
    }
}
