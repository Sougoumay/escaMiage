package m2.miage.interop.servicejeu.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDTO {

    long id;

    long idUtilisateur;

    @Size(min = 2, message = "Le message doit contenir au moins 2 caractères")
    String message;

    String sujet;

    public FeedBackDTO(long id, String message, String sujet) {}
}
