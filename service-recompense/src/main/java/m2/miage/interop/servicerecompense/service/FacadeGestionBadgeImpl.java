package m2.miage.interop.servicerecompense.service;


import m2.miage.interop.servicerecompense.dao.BadgeRepository;
import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.modele.*;
import m2.miage.interop.servicerecompense.modele.enums.Theme;
import m2.miage.interop.servicerecompense.modele.enums.TypeBadge;
import m2.miage.interop.servicerecompense.service.exceptions.*;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionBadge;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class FacadeGestionBadgeImpl implements FacadeGestionBadge {

    private final BadgeRepository badgeRepository;

    // CONSTRUCTEURS
    public FacadeGestionBadgeImpl(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    // METHODES
    @Override
    public List<BadgeDTO> recupererTousBadges() {
        return badgeRepository.findAll().stream()
                .map(x -> new BadgeDTO(
                        x.getNom(),
                        Base64.getEncoder().encodeToString(x.getIcone()),
                        x.getConditionType(),
                        x.getConditionValue()
                ) ).toList();
    }

    @Override
    public BadgeDTO recupererBadgeById(long idBadge) throws PasDeBadgeExistantException {
        Badge badge_recupere = badgeRepository.findById(idBadge).orElse(null);

        if(badge_recupere == null){
            throw new PasDeBadgeExistantException();
        }
        return new BadgeDTO(
                badge_recupere.getId(),
                badge_recupere.getNom(),
                Base64.getEncoder().encodeToString(badge_recupere.getIcone()),
                badge_recupere.getConditionType(),
                badge_recupere.getConditionValue()
        );
    }

    @Override
    public void supprimerBadge(long idBadge) throws PasDeBadgeExistantException {
        if(badgeRepository.existsById(idBadge)){
            badgeRepository.deleteById(idBadge);
        }else {
            throw new PasDeBadgeExistantException();
        }
    }

    @Override
    public BadgeDTO modifierBadge(BadgeDTO badgeDTO, long idBadge) throws InformationManquanteException, InformationIncorrecteException, PasDeBadgeExistantException {
        //check if badge avec idBadge exists
        Optional<Badge> badgeTrouve = badgeRepository.findById(idBadge);

        if(badgeTrouve.isEmpty()){
            throw new PasDeBadgeExistantException();
        }

        //CHECK condition_type
        if(badgeDTO.getConditionType().isBlank() || badgeDTO.getConditionValue() == null){
            throw new InformationManquanteException();
        }

        try {
            TypeBadge.valueOf(badgeDTO.getConditionType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InformationIncorrecteException();
        }

        //CHECK condition_value
        if(badgeDTO.getConditionValue() == null || badgeDTO.getConditionValue().isBlank()){
            throw new InformationManquanteException();
        }
        if (operateurValide(badgeDTO.getConditionValue())){
            throw new InformationIncorrecteException();
        }

        //CHECK icone
        try {
            Base64.getDecoder().decode(badgeDTO.getIcone());
        } catch (IllegalArgumentException e) {
            throw new InformationIncorrecteException();
        }

        Badge badge = badgeRepository.save(new Badge(
                idBadge,
                badgeDTO.getNom(),
                Base64.getDecoder().decode(badgeDTO.getIcone()),
                badgeDTO.getConditionType(),
                badgeDTO.getConditionValue(),
                LocalDate.now()));

        return new BadgeDTO(
                badge.getId(),
                badge.getNom(),
                Base64.getEncoder().encodeToString(badge.getIcone()),
                badge.getConditionType(),
                badge.getConditionValue()
        );
    }

    @Override
    public BadgeDTO creerBadge(BadgeDTO badge) throws TypeBadgeInexistantException, BadgeDejaExistantException, InformationIncorrecteException {

        //verif condition type
        try {
            //verif si condition_type est parmi enum TypeBadge
            Enum.valueOf(TypeBadge.class, badge.getConditionType());
        }catch (IllegalArgumentException e) {
            throw new TypeBadgeInexistantException();
        }

        //verif condition value

            if (Objects.equals(badge.getConditionType(), "THEME")){
                //verif si condition_type est parmi enum TypeBadge
                try{
                    Enum.valueOf(Theme.class, badge.getConditionValue());
                }catch (IllegalArgumentException e) {
                    throw new TypeBadgeInexistantException();
                }
            }
            if (Objects.equals(badge.getConditionType(), "TEMPS_MOYEN") ||
                    Objects.equals(badge.getConditionType(), "MEILLEUR_TEMPS") ||
                    Objects.equals(badge.getConditionType(), "PIRE_TEMPS")){
                //verif si bonne forme valeur
                if (operateurValide(badge.getConditionValue())){
                    throw new InformationIncorrecteException();
                }
            }

        //verif si badge déjà existant
        Badge existe = badgeRepository.findByConditionTypeAndConditionValue(badge.getConditionType(), badge.getConditionValue());
        if(existe != null){
            throw new BadgeDejaExistantException();
        }

        Badge badge_cree = badgeRepository.save(new Badge(
                badge.getNom(),
                Base64.getDecoder().decode(badge.getIcone()),
                badge.getConditionType(),
                badge.getConditionValue()));

        return new BadgeDTO(
                badge_cree.getId(),
                badge_cree.getNom(),
                Base64.getEncoder().encodeToString(badge_cree.getIcone()),
                badge_cree.getConditionType(),
                badge_cree.getConditionValue());
    }


    //METHODES INTERNE
    public boolean operateurValide(String conditionValue) {
        try {
            // Extraction de l'opérateur (garde les symboles "<", "<=", ">", ">=", "==", "!=")
            String operator = conditionValue.replaceAll("[0-9]", "").trim();

            // Extraction du seuil numérique
            String nombre = conditionValue.replaceAll("[^0-9]", "").trim();

            if (nombre.isEmpty()) {
                return true;
            }

            int nb = Integer.parseInt(nombre);

            if (nb<=0){
                return true;
            }

            return !switch (operator) {
                case "<", "<=", ">", ">=", "==", "!=" -> true;
                default -> false;
            };
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
