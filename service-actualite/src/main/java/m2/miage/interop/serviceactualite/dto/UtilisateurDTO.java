package m2.miage.interop.serviceactualite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtilisateurDTO {

    long id;

    String email;

    String pseudo;

    private int scoreSemaine;

    public UtilisateurDTO() {
    }

}
