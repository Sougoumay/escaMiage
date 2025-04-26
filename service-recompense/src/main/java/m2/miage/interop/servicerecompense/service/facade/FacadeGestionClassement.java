package m2.miage.interop.servicerecompense.service.facade;

import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;

import java.util.List;

public interface FacadeGestionClassement {

    List<UtilisateurDTO> recupererClassementGlobal();

    List<UtilisateurDTO> recupererClassementHebdo(int topSouhaite);

    void resetScoresHebdomadaires();
}
