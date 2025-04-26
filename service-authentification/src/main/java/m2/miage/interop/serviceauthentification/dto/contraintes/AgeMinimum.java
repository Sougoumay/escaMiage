package m2.miage.interop.serviceauthentification.dto.contraintes;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeMinimumValidator.class)
@Target({ElementType.FIELD}) // S'applique sur un champ
@Retention(RetentionPolicy.RUNTIME) // Disponible à l'exécution
public @interface AgeMinimum {
    String message() default "L'âge minimum requis n'est pas respecté";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value(); // L'attribut 'value' permet de spécifier l'âge minimum requis
}
