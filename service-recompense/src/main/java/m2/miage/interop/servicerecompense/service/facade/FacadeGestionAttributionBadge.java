package m2.miage.interop.servicerecompense.service.facade;

import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.dto.BadgeUtilisateurDTO;
import m2.miage.interop.servicerecompense.dto.PartieDTO;
import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.modele.BadgeUtilisateur;
import m2.miage.interop.servicerecompense.modele.Statistiques;
import m2.miage.interop.servicerecompense.service.exceptions.AucunUtilisateurTrouveException;
import m2.miage.interop.servicerecompense.service.exceptions.BadgeInexistantException;
import m2.miage.interop.servicerecompense.service.exceptions.PasDeBadgeExistantException;
import m2.miage.interop.servicerecompense.service.exceptions.TypeBadgeInexistantException;

import java.util.List;
import java.util.Optional;

public interface FacadeGestionAttributionBadge {

    void calculBadge(long idUser) throws PasDeBadgeExistantException, TypeBadgeInexistantException;

    Optional<BadgeUtilisateur> attribuerBadge(long idUtilisateur, long idBadge) throws BadgeInexistantException, AucunUtilisateurTrouveException;

    void creerPartie(PartieDTO partieDTO);

    void creerUtilisateur(UtilisateurDTO utilisateurDTO);

    void supprimerUtilisateur(long id);

    Statistiques getStatistiquesUser(long idUser);

    boolean conditionRespectee(String conditionValue, int statValue);

    List<BadgeDTO> recupererMesBadges(long l);
}
