package m2.miage.interop.serviceauthentification.dto.contraintes;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

// Implémentation du validateur
public class AgeMinimumValidator implements ConstraintValidator<AgeMinimum, LocalDate> {
    private int ageMinimum;

    @Override
    public void initialize(AgeMinimum constraintAnnotation) {
        this.ageMinimum = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dateNaissance, ConstraintValidatorContext context) {
        return dateNaissance != null && dateNaissance.isBefore(LocalDate.now().minusYears(ageMinimum));
    }
}