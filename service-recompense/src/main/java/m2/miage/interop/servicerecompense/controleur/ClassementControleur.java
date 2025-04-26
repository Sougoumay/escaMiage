package m2.miage.interop.servicerecompense.controleur;

import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionClassement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/escamiage/recompense")
public class ClassementControleur {


    public final FacadeGestionClassement facadeGestionClassement;

    public ClassementControleur(FacadeGestionClassement facadeGestionClassement) {
        this.facadeGestionClassement = facadeGestionClassement;
    }

    @GetMapping("/classement")
    public ResponseEntity<List<UtilisateurDTO>> recupererClassementGlobal(){
        List<UtilisateurDTO> classement = facadeGestionClassement.recupererClassementGlobal();
        return ResponseEntity.ok(classement);
    }

    @GetMapping("/classement-hebdo")
    public ResponseEntity<List<UtilisateurDTO>> recupererClassementHebdo(){
        List<UtilisateurDTO> classementHebdo = facadeGestionClassement.recupererClassementHebdo(50);
        return ResponseEntity.ok(classementHebdo);
    }
}
