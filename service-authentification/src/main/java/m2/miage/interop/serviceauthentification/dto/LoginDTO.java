package m2.miage.interop.serviceauthentification.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Données de connexion d'un utilisateur")
public record LoginDTO(
        @Schema(description = "Email de l'utilisateur")
        String email,

        @Schema(description = "Mot de passe de l'utilisateur")
        String password
) { }
