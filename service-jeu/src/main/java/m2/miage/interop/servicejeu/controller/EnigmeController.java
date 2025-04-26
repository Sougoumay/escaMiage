package m2.miage.interop.servicejeu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import m2.miage.interop.servicejeu.dto.EnigmeDTO;
import m2.miage.interop.servicejeu.exception.EnigmeInexistanteException;
import m2.miage.interop.servicejeu.exception.FormatEnigmeInvalideException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeEnigme;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/jeu/enigmes")
public class EnigmeController {
    public final FacadeEnigme facadeEnigme;

    public EnigmeController(FacadeEnigme facadeEnigme) {
        this.facadeEnigme = facadeEnigme;
    }

    @Operation(
            summary = "Créer une énigme",
            description = "Ajoute une nouvelle énigme à la base de données. Accessible uniquement aux administrateurs."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Énigme créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Format de l'énigme invalide")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public  ResponseEntity<EnigmeDTO> creerUneEnigme(@RequestBody EnigmeDTO enigmeDTO, UriComponentsBuilder base) {
        EnigmeDTO enigmeCree;
        try {
            enigmeCree = facadeEnigme.creerUneEnigme(enigmeDTO);
        } catch (FormatEnigmeInvalideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        URI location = base.path("api/jeu/enigmes/{idEnigme}")
                .buildAndExpand(enigmeCree.getId())
                .toUri();
        return ResponseEntity.created(location).body(enigmeCree);
    }

    @Operation(summary = "Modifier une énigme", description = "Met à jour une énigme existante. Accessible uniquement aux administrateurs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Énigme modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Format de l'énigme invalide"),
            @ApiResponse(responseCode = "404", description = "Énigme non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idEnigme}")
    public  ResponseEntity<EnigmeDTO> modifierUneEnigme(@PathVariable long idEnigme,@RequestBody EnigmeDTO enigmeDTO, UriComponentsBuilder base) {
        EnigmeDTO enigmeModifie;
        try {
            enigmeModifie = facadeEnigme.modifierUneEnigme(idEnigme,enigmeDTO);
        } catch (FormatEnigmeInvalideException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EnigmeInexistanteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(enigmeModifie);
    }

    @Operation(summary = "Récupérer toutes les énigmes", description = "Retourne la liste de toutes les énigmes. Accessible uniquement aux administrateurs.")
    @ApiResponse(responseCode = "200", description = "Liste des énigmes retournée avec succès")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public  ResponseEntity<List<EnigmeDTO>> getAllEnigmes() {
        return ResponseEntity.ok(facadeEnigme.voirToutesLesEnigmes());
    }

    @Operation(summary = "Supprimer une énigme", description = "Supprime une énigme existante. Accessible uniquement aux administrateurs.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Énigme supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Énigme non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idEnigme}")
    public ResponseEntity<Void> supprimerUneEnigme(@PathVariable long idEnigme) {
        try {
            facadeEnigme.supprimerUneEnigme(idEnigme);
        } catch (EnigmeInexistanteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer une énigme par ID", description = "Retourne une énigme en fonction de son ID. Accessible uniquement aux administrateurs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Énigme trouvée"),
            @ApiResponse(responseCode = "404", description = "Énigme non trouvée")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idEnigme}")
    public ResponseEntity<EnigmeDTO> getEnigmeById(@PathVariable long idEnigme) {
        EnigmeDTO enigme;
        try{
            enigme = facadeEnigme.getEnigmeById(idEnigme);
        }catch (EnigmeInexistanteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(enigme);
    }








}
