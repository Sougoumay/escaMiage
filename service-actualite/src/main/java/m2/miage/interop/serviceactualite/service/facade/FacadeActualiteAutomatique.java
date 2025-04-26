package m2.miage.interop.serviceactualite.service.facade;

import m2.miage.interop.serviceactualite.dto.BadgeUtilisateurDTO;
import m2.miage.interop.serviceactualite.dto.UtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.TemplateInexistantEXception;

import java.util.List;

public interface FacadeActualiteAutomatique {

    void genererPostBadge(List<BadgeUtilisateurDTO> badgeUtilisateur) throws TemplateInexistantEXception;

    void genererPostClassement(List<UtilisateurDTO> utilisateursDTO);

}
