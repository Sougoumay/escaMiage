package m2.miage.interop.servicerecompense.service.facade;

import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.service.exceptions.*;
import java.util.List;

public interface FacadeGestionBadge {
    List<BadgeDTO> recupererTousBadges();

    BadgeDTO creerBadge(BadgeDTO badge) throws TypeBadgeInexistantException, BadgeDejaExistantException, InformationIncorrecteException;

    BadgeDTO recupererBadgeById(long idBadge) throws PasDeBadgeExistantException;

    void supprimerBadge(long idBadge) throws PasDeBadgeExistantException;

    BadgeDTO modifierBadge(BadgeDTO badgeDTO, long idBadge) throws InformationManquanteException, InformationIncorrecteException, PasDeBadgeExistantException;

}
