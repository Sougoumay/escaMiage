package m2.miage.interop.servicerecompense.controleur;

import jakarta.validation.Valid;
import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionAttributionBadge;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionBadge;
import m2.miage.interop.servicerecompense.service.exceptions.*;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionClassement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/escamiage/recompense/badge")
public class BadgeControleur {

    public final FacadeGestionBadge facadeGestionBadge;
    public final FacadeGestionAttributionBadge facadeGestionAttributionBadge;

    public BadgeControleur(FacadeGestionBadge facadeGestionBadge, FacadeGestionAttributionBadge facadeGestionAttributionBadge) {
        this.facadeGestionBadge = facadeGestionBadge;
        this.facadeGestionAttributionBadge = facadeGestionAttributionBadge;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idBadge}")
    public ResponseEntity<BadgeDTO> recupererBadgeById(@PathVariable String idBadge){
        long idBadgeFormat;
        BadgeDTO badgeDTO;
        try {
            idBadgeFormat = Long.parseLong(idBadge);
            badgeDTO = facadeGestionBadge.recupererBadgeById(idBadgeFormat);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (PasDeBadgeExistantException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(badgeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idBadge}")
    public ResponseEntity<Void> supprimerBadgeById(@PathVariable String idBadge){
        long idBadgeFormat;
        try {
            idBadgeFormat = Long.parseLong(idBadge);
            facadeGestionBadge.supprimerBadge(idBadgeFormat);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (PasDeBadgeExistantException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idBadge}")
    public ResponseEntity<BadgeDTO> modifierBadge(@Valid @PathVariable String idBadge, @RequestBody BadgeDTO badge){
        long numeric_id;
        try{
            numeric_id = Long.parseLong(idBadge);
            BadgeDTO badgeDTO = facadeGestionBadge.modifierBadge(badge, numeric_id);
            return ResponseEntity.ok(badgeDTO);
        } catch (NumberFormatException | InformationManquanteException | InformationIncorrecteException e) {
            return ResponseEntity.badRequest().build();
        } catch (PasDeBadgeExistantException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<BadgeDTO>> recupererTousBadges() {
        return ResponseEntity.ok(facadeGestionBadge.recupererTousBadges());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<BadgeDTO> creerBadge(@RequestBody BadgeDTO badge, UriComponentsBuilder base) {

        try{
            BadgeDTO badge_cree = facadeGestionBadge.creerBadge(badge);
            URI location = base.path("/escamiage/recompense/{idBadge}")
                    .buildAndExpand(badge_cree.getId())
                    .toUri();
            return ResponseEntity.created(location)
                    .body(badge_cree);
        } catch (TypeBadgeInexistantException | InformationIncorrecteException e) {
            return ResponseEntity.badRequest().build();
        } catch (BadgeDejaExistantException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/mes-badges")
    public ResponseEntity<List<BadgeDTO>> recupererMesBadges(Authentication authentication, UriComponentsBuilder base) {
        List<BadgeDTO> mesBadges = facadeGestionAttributionBadge.recupererMesBadges(Long.parseLong(authentication.getName()));
        return ResponseEntity.ok(mesBadges);
    }





}
