package m2.miage.interop.serviceauthentification.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UtilisateurCreationDTO(
        @Email(message = "E-mail invalide")
        String email,

        @Size(min = 12, message = "Le mot de passe doit contenir au moins 12 caractères")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
                message = "Le mot de passe doit contenir une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial (@#$%^&+=)"
        )
        String password,

        @Size(min = 2, message = "Le nom doit contenir au moins 2 caractères")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\s]+$",
                message = "Le nom ne doit contenir que des lettres, espaces, apostrophes ou traits d'union"
        )
        String nom,

        @Size(min = 2, message = "Le prénom doit contenir au moins 2 caractères")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\s]+$",
                message = "Le prénom ne doit contenir que des lettres, espaces, apostrophes ou traits d'union"
        )
        String prenom,

        @Size(min = 2, message = "Le pseudo doit contenir au moins 2 caractères")
        String pseudo,

        @Past(message = "La date de naissance doit être dans le passé")
        LocalDate dateNaissance,

        byte[] image
) {
    @AssertTrue(message = "L'utilisateur doit avoir au moins 18 ans")
    public boolean isDateNaissanceValide() {
        return dateNaissance != null && dateNaissance.isBefore(LocalDate.now().minusYears(18));
    }
}
