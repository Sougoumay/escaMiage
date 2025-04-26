package m2.miage.interop.servicejeu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import m2.miage.interop.servicejeu.dto.PartieDTO;
import m2.miage.interop.servicejeu.dto.ReponseDTO;
import m2.miage.interop.servicejeu.dto.StatistiqueDTO;
import m2.miage.interop.servicejeu.exception.ActionNonAutoriseException;
import m2.miage.interop.servicejeu.exception.PartieNotFoundException;
import m2.miage.interop.servicejeu.exception.ReponseNotFoundException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadePartie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/jeu/parties")
public class PartieController {

    private final FacadePartie facadePartie;


    public PartieController(FacadePartie facadePartie) {
        this.facadePartie = facadePartie;
    }

    @Operation(summary = "Démarrer une nouvelle partie", description = "Crée une nouvelle partie pour l'utilisateur authentifié.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @PostMapping
    public ResponseEntity<List<ReponseDTO>> demarrerPartie(Authentication authentication, UriComponentsBuilder builder)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        List<ReponseDTO> dtos = facadePartie.creerPartie(idUtilisateur);
        long partieId = dtos.getFirst().getPartieId();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                builder
                        .path("/api/jeu/parties/{id}")
                        .buildAndExpand(partieId)
                        .toUri()
        );

        return new ResponseEntity<>(dtos, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('JOUEUR')")
    @GetMapping("/{idPartie}")
    public ResponseEntity<PartieDTO> recupererPartie(@PathVariable long idPartie,Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        try {
            PartieDTO partieDTO = facadePartie.recupererPartie(idPartie,idUtilisateur);
            return ResponseEntity.ok(partieDTO);
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Annuler une partie", description = "Permet à l'utilisateur d'annuler une partie en cours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partie annulée avec succès"),
            @ApiResponse(responseCode = "404", description = "Partie non trouvée", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @PutMapping("/{idPartie}")
    public ResponseEntity<String> annulerPartie(@PathVariable long idPartie, Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        try {
            facadePartie.quitterPartie(idPartie,idUtilisateur);
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public record UserAnswer(String answer){}

    @Operation(summary = "Répondre à une énigme", description = "Soumet une réponse à une énigme dans une partie en cours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réponse enregistrée avec succès"),
            @ApiResponse(responseCode = "404", description = "Partie ou énigme non trouvée", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @PutMapping("{idPartie}/enigmes/{idReponse}")
    public ResponseEntity<ReponseDTO>  repondreAUnEnigme(@PathVariable long idPartie, @PathVariable long idReponse, @RequestBody UserAnswer userAnswer, Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        ReponseDTO result = null;
        try {
            result = facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idReponse, userAnswer.answer());
        } catch (ReponseNotFoundException | PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Consulter une énigme", description = "Permet de récupérer une énigme d'une partie en cours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Énigme retournée avec succès"),
            @ApiResponse(responseCode = "404", description = "Partie ou énigme non trouvée", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @GetMapping("{idPartie}/enigmes/{idReponse}")
    public ResponseEntity<ReponseDTO> consulterEnigme(@PathVariable long idPartie, @PathVariable long idReponse, Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        ReponseDTO dto = null;
        try {
            dto = facadePartie.consulterEnigme(idUtilisateur,idPartie, idReponse);
        } catch (ReponseNotFoundException | PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Répondre à la question Master", description = "Soumet une réponse à la question Master d'une énigme.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tentative de réponse enregistrée avec succès. Si la réponse est correcte, la valeur du retour de la fonction est true, sinon la fonction retournera false"),
            @ApiResponse(responseCode = "404", description = "Partie ou énigme non trouvée", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @PutMapping("{idPartie}/master")
    public ResponseEntity<PartieDTO> repondreALaQuestionMaster(@PathVariable long idPartie, @RequestBody UserAnswer userAnswer, Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        PartieDTO partieDTO = null;
        try {
            partieDTO = facadePartie.repondreALaQuestionMaster(idUtilisateur,idPartie, userAnswer.answer());
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(partieDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{idPartie}/master/indice")
    public ResponseEntity<Integer> recupererIndiceMasterQuestion(@PathVariable long idPartie)
    {
        try {
            int indiceMasterQuestion = facadePartie.getMasterIndice(idPartie);
            return ResponseEntity.ok(indiceMasterQuestion);
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('JOUEUR')")
    @GetMapping("{idPartie}/stats")
    public ResponseEntity<StatistiqueDTO> recupererStatistiquesPartie(@PathVariable long idPartie,Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        StatistiqueDTO statistiqueDTO = null;
        try {
            statistiqueDTO = facadePartie.recupererStatistiquesPartie(idUtilisateur,idPartie);
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(statistiqueDTO);
    }


    @Operation(summary = "Terminer une partie", description = "Clôture une partie et envoie les résultats via RabbitMQ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partie terminée avec succès"),
            @ApiResponse(responseCode = "404", description = "Partie non trouvée", content = @Content),
            @ApiResponse(responseCode = "406", description = "Action non autorisée", content = @Content)
    })
    @PreAuthorize("hasRole('JOUEUR')")
    @PutMapping("{idPartie}/fin")
    public ResponseEntity<String> terminerPartie(@PathVariable long idPartie, Authentication authentication)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        try {
            facadePartie.terminerPartie(idUtilisateur,idPartie);
        } catch (PartieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ActionNonAutoriseException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok("Vous avez bien cloturé votre partie");
    }

}
