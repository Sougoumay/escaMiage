package m2.miage.interop.serviceactualite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeDTO {

    long id;

    String nom;

    String conditionType;

    String conditionValue;

    public BadgeDTO() {
    }

    public BadgeDTO(long id, String nom, String conditionType, String conditionValue) {
        this.id = id;
        this.nom = nom;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
    }
}
