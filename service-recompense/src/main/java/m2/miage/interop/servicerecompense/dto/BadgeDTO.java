package m2.miage.interop.servicerecompense.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeDTO {


    long id;

    @NotBlank(message = "Le nom du badge est obligatoire.")
    String nom;

    @NotBlank(message = "L'icône est obligatoire et doit être encodée en Base64.")
    String icone;

    String conditionType;

    String conditionValue;

    public BadgeDTO() {
    }

    public BadgeDTO(String nom, String icone, String conditionType, String conditionValue) {
        this.nom = nom;
        this.icone = icone;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
    }

    public BadgeDTO(long id, String nom, String icone, String conditionType, String conditionValue) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
    }

    public BadgeDTO(long id, String conditionType, String conditionValue, String nom) {
        this.id = id;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.nom = nom;
    }
}
