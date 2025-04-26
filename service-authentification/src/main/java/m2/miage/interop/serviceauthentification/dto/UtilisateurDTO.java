package m2.miage.interop.serviceauthentification.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.serviceauthentification.dto.contraintes.AgeMinimum;
import java.time.LocalDate;

@Getter
@Setter
public class UtilisateurDTO{
        long id;

        @Email(message = "E-mail invalide")
        String email;

        @Size(min = 2, message = "Le nom doit contenir au moins 2 caractères")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\s]+$",
                message = "Le nom ne doit contenir que des lettres, espaces, apostrophes ou traits d'union"
        )
        String nom;

        @Size(min = 2, message = "Le prénom doit contenir au moins 2 caractères")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\s]+$",
                message = "Le prénom ne doit contenir que des lettres, espaces, apostrophes ou traits d'union"
        )
        String prenom;

        @Past(message = "La date de naissance doit être dans le passé")
        @AgeMinimum(value = 18, message = "L'utilisateur doit avoir au moins 18 ans")
        LocalDate dateNaissance;

        @Size(min = 2, message = "Le pseudo doit contenir au moins 2 caractères")
        String pseudo;

        byte[] image;

        String role;

        public UtilisateurDTO() {
        }

        public UtilisateurDTO(long id) {
                this.id = id;
        }

        public UtilisateurDTO(long id, String email, String pseudo, String nom, String prenom, byte[] image, LocalDate dateNaissance, String role) {
                this.id = id;
                this.email = email;
                this.pseudo = pseudo;
                this.nom = nom;
                this.prenom = prenom;
                this.image = image;
                this.dateNaissance = dateNaissance;
                this.role = role;
        }
}
