package m2.miage.interop.serviceauthentification.controleur;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import m2.miage.interop.serviceauthentification.config.TypeToken;
import m2.miage.interop.serviceauthentification.dto.LoginDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurCreationDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceauthentification.service.FacadeUtilisateur;
import m2.miage.interop.serviceauthentification.exceptions.FormatInvalideException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurDejaExistantException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@RestController
@RequestMapping("/escamiage/utilisateur")
@Tag(name = "API Utilisateur", description = "API pour la gestion des utilisateurs du service Escamiage")
public class UtilisateurControleur {

    private final FacadeUtilisateur facadeUtilisateur;

    private final PasswordEncoder passwordEncoder;

    private final BiFunction<UtilisateurDTO, TypeToken, String> genereToken;


    public UtilisateurControleur(FacadeUtilisateur facadeUtilisateur, PasswordEncoder passwordEncoder, BiFunction<UtilisateurDTO, TypeToken, String> genereToken) {
        this.facadeUtilisateur = facadeUtilisateur;
        this.passwordEncoder = passwordEncoder;
        this.genereToken = genereToken;
    }

    @Operation(summary = "Inscrire un nouvel utilisateur", description = "Permet d'inscrire un nouvel utilisateur dans le système et retourne un token JWT d'authentification")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
                    content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Les données fournies sont incorrectes ou mal formatées"),
            @ApiResponse(responseCode = "409", description = "Conflit - Un utilisateur avec cet email existe déjà")
    })
    @PreAuthorize("permitAll()")
    @PostMapping()
    public ResponseEntity<UtilisateurDTO> inscrire(@Valid @RequestBody UtilisateurCreationDTO user, BindingResult result, UriComponentsBuilder base) {
        //validations
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        user = new UtilisateurCreationDTO(
                user.email(),
                passwordEncoder.encode(user.password()),
                user.nom(),
                user.prenom(),
                user.pseudo(),
                user.dateNaissance(),
                user.image()
        );
        try {
            UtilisateurDTO inscrit = facadeUtilisateur.inscrireUtilisateur(user);
            URI location = base.path("/escamiage/utilisateur/{idUtilisateur}")
                    .buildAndExpand(inscrit.getId())
                    .toUri();
            return ResponseEntity.created(location)
                    .header("Access-Control-Expose-Headers", "Authorization")
                    .header("Authorization", "Bearer " + genereToken.apply(inscrit,TypeToken.ACCESS_TOKEN))
                    .header("Refresh-token", "Bearer " + genereToken.apply(inscrit,TypeToken.REFRESH_TOKEN))
                    .body(inscrit);
        } catch (FormatInvalideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UtilisateurDejaExistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @Operation(summary = "Connexion d'un utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - Le mot de passe est incorrect"),
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/connexion")
    public ResponseEntity<Long> connexion(@Valid @RequestBody LoginDTO datalogin, BindingResult result){
        UtilisateurDTO utilisateurDTO = null;
        //validations
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            utilisateurDTO = facadeUtilisateur.connexionUtilisateur(datalogin);
        } catch (FormatInvalideException | UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization","Bearer "+genereToken.apply(utilisateurDTO,TypeToken.ACCESS_TOKEN))
                .header("Access-Control-Expose-Headers", "Authorization")
                .header("Refresh-token", "Bearer " + genereToken.apply(utilisateurDTO,TypeToken.REFRESH_TOKEN))
                .body(utilisateurDTO.getId());

    }



    @Operation(summary = "Récupérer tous les utilisateurs", description = "Récupère la liste de tous les utilisateurs (réservé aux administrateurs)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès",
                    content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @ApiResponse(responseCode = "401", description = "Non autorisé - L'utilisateur n'est pas authentifié"),
            @ApiResponse(responseCode = "403", description = "Interdit - L'utilisateur n'a pas les droits d'administrateur")
    })
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UtilisateurDTO>> recupererTousLesUtilisateurs() {
        return ResponseEntity.ok(facadeUtilisateur.getTousLesUtilisateurs());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UtilisateurDTO> creerUnAdmin(@Valid @RequestBody UtilisateurCreationDTO utilisateurCreationDTO, BindingResult result, UriComponentsBuilder base) {
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        utilisateurCreationDTO = new UtilisateurCreationDTO(
                utilisateurCreationDTO.email(),
                passwordEncoder.encode(utilisateurCreationDTO.password()),
                utilisateurCreationDTO.nom(),
                utilisateurCreationDTO.prenom(),
                utilisateurCreationDTO.pseudo(),
                utilisateurCreationDTO.dateNaissance(),
                utilisateurCreationDTO.image()
        );
        try {
            UtilisateurDTO inscrit = facadeUtilisateur.creerUnAdmin(utilisateurCreationDTO);
            URI location = base.path("/escamiage/utilisateur/{idUtilisateur}")
                    .buildAndExpand(inscrit.getId())
                    .toUri();
            return ResponseEntity.created(location)
                    .body(inscrit);
        } catch (FormatInvalideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (UtilisateurDejaExistantException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @Operation(summary = "Récupérer un profil utilisateur", description = "Récupère les détails d'un profil utilisateur spécifique")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès",
                    content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Format d'ID invalide"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - L'utilisateur n'est pas authentifié"),
            @ApiResponse(responseCode = "403", description = "Interdit - L'utilisateur n'a pas le droit d'accéder à ce profil"),
            @ApiResponse(responseCode = "404", description = "Non trouvé - L'utilisateur demandé n'existe pas")
    })
    @PreAuthorize("#idUtilisateur == authentication.name")
    @GetMapping("/{idUtilisateur}")
    public ResponseEntity<UtilisateurDTO> recupererMonProfil(@PathVariable String idUtilisateur, Authentication authentication){
        UtilisateurDTO utilisateurDTO = facadeUtilisateur.getUtilisateurById(Long.parseLong(idUtilisateur));
        return ResponseEntity.ok(utilisateurDTO);
    }

    @Operation(summary = "Modifier un profil utilisateur", description = "Met à jour les informations d'un profil utilisateur existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès. Retourne le profil mis à jour",
                    content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Données incorrectes ou mal formatées"),
            @ApiResponse(responseCode = "401", description = "Non autorisé - L'utilisateur n'est pas authentifié"),
            @ApiResponse(responseCode = "403", description = "Interdit - L'utilisateur n'a pas le droit de modifier ce profil"),
            @ApiResponse(responseCode = "404", description = "Non trouvé - L'utilisateur à modifier n'existe pas"),
            @ApiResponse(responseCode = "409", description = "Conflit - L'email fourni est déjà utilisé par un autre utilisateur")
    })
    @PreAuthorize("hasRole('ADMIN') or #idUtilisateur == authentication.name")
    @PutMapping("/{idUtilisateur}")
    public ResponseEntity<?> modifyProfile(@PathVariable String idUtilisateur, @Valid @RequestBody UtilisateurDTO updatedUser,BindingResult result, Authentication authentication) {
        //La mise à jour du mot de passe doit passer pas autre part ici ce sont les informations relatives au profil
        // si erreurs de validation
        if (result.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UtilisateurDTO updatedProfile;
        try {
            updatedProfile = facadeUtilisateur.updateUser(Long.parseLong(idUtilisateur),updatedUser);
        } catch (FormatInvalideException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable"); // 404 Not Found
        }
        return ResponseEntity.ok(updatedProfile);
    }


    @PreAuthorize("hasRole('ADMIN') or #idUtilisateur == authentication.name")
    @DeleteMapping("/{idUtilisateur}")
    public ResponseEntity<String> supprimerUnUtilisateur(@PathVariable String idUtilisateur, Authentication authentication) {
        facadeUtilisateur.supprimerUtilisateur(Long.parseLong(idUtilisateur));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN') or #idUtilisateur == authentication.name")
    @PutMapping("/{idUtilisateur}/mot-de-passe")
    public ResponseEntity<String> modifierMotDePasse(@PathVariable String idUtilisateur, @RequestBody MotDePasse motDePasse, Authentication authentication) {
        facadeUtilisateur.modifierMotDePasse(Long.parseLong(idUtilisateur),passwordEncoder.encode(motDePasse.motDePasse));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/password-reset/token")
    public ResponseEntity<Void> genererCode(@RequestBody EmailRecord body) {
        facadeUtilisateur.genererCode(body.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/password-reset/verification")
    public ResponseEntity<Boolean> verifierCode(@RequestBody Token body) {
        boolean result = facadeUtilisateur.verifierCode(body.code(), body.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);

    }

    @PutMapping("/password-reset/modififcation")
    public ResponseEntity<String> resetMotDePasse(@RequestBody MotDePasse motDePasse) {
        facadeUtilisateur.modifierMotDePasse(motDePasse.email(),passwordEncoder.encode(motDePasse.motDePasse));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public record EmailRecord(@Email(message = "Email invalide") String email){}

    public record Token(long code, @Email(message = "Email invalide") String email){}

    //Entités propre au controller
    public record MotDePasse(  @Size(min = 12, message = "Le mot de passe doit contenir au moins 12 caractères")
                                  @Pattern(
                                          regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
                                          message = "Le mot de passe doit contenir une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial (@#$%^&+=)"
                                  )String motDePasse, @Email(message = "Email invalide") String email){}




}
