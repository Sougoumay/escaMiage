package m2.miage.interop.servicejeu.controller;

import jakarta.validation.Valid;
import m2.miage.interop.servicejeu.dto.FeedBackDTO;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeExtraJeu;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/jeu/feedback")
public class ExtraJeuController {

    public final FacadeExtraJeu facadeExtraJeu;

    public ExtraJeuController(FacadeExtraJeu facadeExtraJeu) {
        this.facadeExtraJeu = facadeExtraJeu;
    }

    @PostMapping("")
    public ResponseEntity<FeedBackDTO> donnerFeedBack(@Valid @RequestBody FeedBackDTO feedBack, Authentication authentication, UriComponentsBuilder builder)
    {
        long idUtilisateur = Long.parseLong(authentication.getName());
        try{
            FeedBackDTO feedBackDTO = facadeExtraJeu.donnerFeedBack(idUtilisateur,feedBack);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                    builder
                            .path("/api/jeu/feedback/{idFeedback}")
                            .buildAndExpand(feedBackDTO.getId())
                            .toUri()
            );
            return new ResponseEntity<>(feedBackDTO, headers, HttpStatus.CREATED);

        } catch ( IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
