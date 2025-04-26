package m2.miage.interop.serviceactualite.controlleur;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import m2.miage.interop.serviceactualite.dto.PostDTO;
import m2.miage.interop.serviceactualite.dto.ReactionUtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.ContenuPostInvalideException;
import m2.miage.interop.serviceactualite.exceptions.PostInexistantException;
import m2.miage.interop.serviceactualite.exceptions.ReactionUtilisateurIncorrectException;
import m2.miage.interop.serviceactualite.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceactualite.service.facade.FacadeActualite;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/escamiage/actualite")
@Tag(name = "API Actualités", description = "API pour la gestion des actualités du service Escamiage")
public class ActualiteController {

    public final FacadeActualite facadeActualite;

    public ActualiteController(FacadeActualite facadeActualite) {
        this.facadeActualite = facadeActualite;
    }

    @Operation(summary = "Récupérer tous les posts", description = "Récupère la liste des posts d'actualité avec pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des posts récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = PostDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PostDTO>> recupererMesPosts(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(facadeActualite.recupererMesPosts(Long.parseLong(authentication.getName())));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idPost}")
    public ResponseEntity<PostDTO> getUnPost(@PathVariable int idPost) {
        PostDTO post;
        try {
            post = facadeActualite.recupererUnPost(idPost);
        } catch (PostInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @Operation(summary = "Réagir à un post", description = "Permet à un utilisateur d'ajouter une réaction à un post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réaction ajoutée avec succès"),
            @ApiResponse(responseCode = "404", description = "L'utilisateur ou post non existant"),
            @ApiResponse(responseCode = "400", description = "Requête invalide - L'utilisateur, le post ou la réaction est invalide")
    })
    @PutMapping("/{idPost}/reaction")
    public ResponseEntity<PostDTO> reagirAUnPost(@PathVariable long idPost,@RequestBody ReactionUtilisateurDTO reactionUtilisateurDTO, Authentication authentication) {
        long idUtilisateur =  Long.parseLong(authentication.getName());
        try {
            facadeActualite.reagirAUnPost(idPost,idUtilisateur,reactionUtilisateurDTO);
        } catch (ReactionUtilisateurIncorrectException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (UtilisateurInexistantException |  PostInexistantException  e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Publier un post", description = "Permet à un administrateur de publier un nouveau post d'actualité")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post créé avec succès",
                    content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Le contenu du post est invalide ou l'utilisateur n'existe pas"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - L'utilisateur n'a pas les droits d'administrateur")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> posterUnPost(@RequestBody PostDTO postDTO, UriComponentsBuilder base, Authentication authentication) {
        PostDTO postDTOCree;
        try {
            long idUtilisateur =  Long.parseLong(authentication.getName());
            postDTOCree = facadeActualite.ajouterPost(postDTO,idUtilisateur);
        } catch (ContenuPostInvalideException | UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        URI location = base.path("/escamiage/actualite/{id}")
                .buildAndExpand(postDTOCree.getIdPost())
                .toUri();
        return ResponseEntity.created(location).body(postDTOCree);
    }

    @Operation(summary = "Supprimer un post", description = "Permet à un administrateur de supprimer un post d'actualité existant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Post non trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - L'utilisateur n'a pas les droits d'administrateur")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerUnPost(@PathVariable int id) {
        try {
            facadeActualite.supprimerUnPost(id);
        } catch (PostInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Modifier un post", description = "Permet à un administrateur de modifier un post d'actualité existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide - Le post n'existe pas ou le contenu est invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - L'utilisateur n'a pas les droits d'administrateur")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierUnPost(@PathVariable int id,@RequestBody PostDTO postDTO) {
        try {
            facadeActualite.modifierUnPost(id, postDTO);
        } catch (PostInexistantException  e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
