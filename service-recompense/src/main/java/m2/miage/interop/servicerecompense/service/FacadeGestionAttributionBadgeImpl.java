package m2.miage.interop.servicerecompense.service;

import m2.miage.interop.servicerecompense.dao.*;
import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.dto.BadgeUtilisateurDTO;
import m2.miage.interop.servicerecompense.dto.PartieDTO;
import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.modele.*;
import m2.miage.interop.servicerecompense.modele.enums.Theme;
import m2.miage.interop.servicerecompense.service.exceptions.AucunUtilisateurTrouveException;
import m2.miage.interop.servicerecompense.service.exceptions.BadgeInexistantException;
import m2.miage.interop.servicerecompense.service.exceptions.PasDeBadgeExistantException;
import m2.miage.interop.servicerecompense.service.exceptions.TypeBadgeInexistantException;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionAttributionBadge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class FacadeGestionAttributionBadgeImpl implements FacadeGestionAttributionBadge {

    private final BadgeRepository badgeRepository;
    private final PartieRepository partieRepository;
    private final BadgeUtilisateurRepository badgeUtilisateurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RabbitMqSender rabbitMqSender;
    private static final Logger logger = LoggerFactory.getLogger(FacadeGestionAttributionBadgeImpl.class);
    private final StatistiquesRepository statistiquesRepository;

    public FacadeGestionAttributionBadgeImpl(BadgeRepository badgeRepository, PartieRepository partieRepository, BadgeUtilisateurRepository badgeUtilisateurRepository, UtilisateurRepository utilisateurRepository, RabbitMqSender rabbitMqSender, StatistiquesRepository statistiquesRepository) {
        this.badgeRepository = badgeRepository;
        this.partieRepository = partieRepository;
        this.badgeUtilisateurRepository = badgeUtilisateurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.rabbitMqSender = rabbitMqSender;
        this.statistiquesRepository = statistiquesRepository;
    }

    @Override
    public void calculBadge(long idUser){

        Statistiques statsUtilisateur = getStatistiquesUser(idUser);
        List<Badge> listeBadges = badgeRepository.findAll();
        List<BadgeUtilisateurDTO> nouveauxBadgesDTO = new ArrayList<>();

        if (listeBadges.isEmpty()) {
            logger.error("Aucun badge en base de donnée");
            return; //on sort de la méthode
        }

        for (Badge badge : listeBadges) {
            boolean badgeAttribue = switch (badge.getConditionType()) {
                case "TEMPS_MOYEN" ->
                        conditionRespectee(badge.getConditionValue(), statsUtilisateur.getDureeMoyennePartie());
                case "MEILLEUR_TEMPS" ->
                        conditionRespectee(badge.getConditionValue(), statsUtilisateur.getMeilleurTemps());
                case "TENTATIVES" ->
                        conditionRespectee(badge.getConditionValue(), statsUtilisateur.getNbTentativesGlobal());
                case "PIRE_TEMPS" -> conditionRespectee(badge.getConditionValue(), statsUtilisateur.getPireTemps());
                case "THEME" -> {
                    //recup tous les thémes vus par le user
                    Set<Theme> themesRencontres = new HashSet<>(statsUtilisateur.getThemes());
                    //voir si dans themes vus, le theme demandé par badge est présent
                    yield themesRencontres.contains(Theme.valueOf(badge.getConditionType()));
                }
                default -> false; //logger.error("Type de badge inexistant");
            };

            if (badgeAttribue) {
                try {
                    Optional<BadgeUtilisateur> nouveauBadge = attribuerBadge(idUser, badge.getId());
                    nouveauBadge.ifPresent(badgeUtilisateur ->
                            nouveauxBadgesDTO.add(new BadgeUtilisateurDTO(
                                    badgeUtilisateur.getUtilisateur().getId(),
                                    new BadgeDTO(
                                            badgeUtilisateur.getBadge().getId(),
                                            badgeUtilisateur.getBadge().getConditionType(),
                                            badgeUtilisateur.getBadge().getConditionValue(),
                                            badgeUtilisateur.getBadge().getNom()
                                    )
                            ))
                    );

                } catch (BadgeInexistantException e) {
                    logger.error("Le badge recherché est inexistant", e);
                } catch (AucunUtilisateurTrouveException e) {
                    logger.error("Aucun utilisateur n'a été trouvé", e);
                }
            }
        }

        if(!nouveauxBadgesDTO.isEmpty()){
            rabbitMqSender.envoyerBadgeUtilisateur(nouveauxBadgesDTO);
        }
    }

    /**
     * Création d'une partie associé à l'utilisateur et calcul des nouveaux badges associés
     * @param partieDto : Récupérer via un appel Rabbit
     */
    @Override
    public void creerPartie (PartieDTO partieDto){
        partieRepository.save(new Partie(
                partieDto.getId(),
                partieDto.getIdUtilisateur(),
                partieDto.getDureePartieEnSecondes(),
                partieDto.getNbTentativesTotal(),
                partieDto.getScore(),
                partieDto.getReponsesCorrectes(),
                partieDto.getThemes().stream().map(Theme::valueOf).toList(),
                partieDto.isReussie(),
                LocalDate.now()
        ));
        calculBadge(partieDto.getIdUtilisateur());
    }

    @Override
    public void creerUtilisateur(UtilisateurDTO utilisateurDTO){
        utilisateurRepository.save(new Utilisateur(
                utilisateurDTO.getId(),
                utilisateurDTO.getMeilleurScore(),
                utilisateurDTO.getMeilleurTemps(),
                utilisateurDTO.getDateDernierePartie(),
                utilisateurDTO.getNbPartiesJouees(),
                utilisateurDTO.getScoreSemaine()
        ));
    }

    @Transactional
    @Override
    public void supprimerUtilisateur(long id){
        utilisateurRepository.deleteById(id);
        statistiquesRepository.deleteAllByIdUtilisateur(id);
        partieRepository.deleteAllByIdUtilisateur(id);
    }

    @Override
    public Optional<BadgeUtilisateur> attribuerBadge(long idUtilisateur, long idBadge) throws BadgeInexistantException, AucunUtilisateurTrouveException {
        Optional<BadgeUtilisateur> existingBadge = badgeUtilisateurRepository.findByUtilisateurIdAndBadgeId(idUtilisateur, idBadge);
        if (existingBadge.isPresent()) {
            return Optional.empty();
        }
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurById(idUtilisateur)
                .orElseThrow(AucunUtilisateurTrouveException::new);

        Badge badge = badgeRepository.findBadgeById(idBadge)
                .orElseThrow(BadgeInexistantException::new);
        return Optional.of(badgeUtilisateurRepository.save(new BadgeUtilisateur(utilisateur, badge)));
    }

    @Override
    public List<BadgeDTO> recupererMesBadges(long idUtilisateur) {
        List<BadgeUtilisateur> mesBadges = badgeUtilisateurRepository.findByUtilisateurId(idUtilisateur);
        List<Badge> badges = mesBadges.stream().map(BadgeUtilisateur::getBadge).toList();
        return badges.stream().map(badge -> new BadgeDTO(badge.getId(),badge.getNom(), Arrays.toString(badge.getIcone()),badge.getConditionType(),badge.getConditionValue())).toList();
    }

    @Override
    public boolean conditionRespectee(String conditionValue, int statValue) {
        // Gestion des conditions avec opérateurs "<", "<=", ">", ">=", "==", "!="
        String operator = conditionValue.replaceAll("[0-9]", "").trim();
        int threshold = Integer.parseInt(conditionValue.replaceAll("[^0-9]", "").trim());

        return switch (operator) {
            case "<"  -> statValue < threshold;
            case "<=" -> statValue <= threshold;
            case ">"  -> statValue > threshold;
            case ">=" -> statValue >= threshold;
            case "==" -> statValue == threshold;
            case "!=" -> statValue != threshold;
            default   -> false;
        };
    }


    @Override
    public Statistiques getStatistiquesUser(long idUser) {
        List<Partie> historique = partieRepository.findByIdUtilisateur(idUser);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(idUser);

        if (historique.isEmpty()) {
            return new Statistiques(idUser, 0, 0, 0, 0, 0, null, 0);
        }

        int totalTentatives = 0;
        int totalDuree = 0;
        int partiesTerminees = 0;
        int meilleurTemps = Integer.MAX_VALUE;
        int meilleurScore = 0;
        int pireTemps = 0;
        int scoreSemaine = 0;
        Set<Theme> listeThemesVus = new HashSet<>();

        // Calculer la date de début de la semaine (lundi)
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // Lundi de cette semaine

        for (Partie partie : historique) {
            totalTentatives += partie.getNbTentativesTotal();
            totalDuree += partie.getDureePartieEnSecondes();
            partiesTerminees++;

            //ajout des themes vus dans liste globale
            listeThemesVus.addAll(partie.getThemes());

            if (partie.getDureePartieEnSecondes() < meilleurTemps) {
                meilleurTemps = partie.getDureePartieEnSecondes();
            }
            if (partie.getDureePartieEnSecondes() > pireTemps) {
                pireTemps = partie.getDureePartieEnSecondes();
            }
            if (partie.getScore() > meilleurScore) {
                meilleurScore = partie.getScore();
            }

            // Calculer le score de la semaine
            if (partie.getDatePartie().isAfter(startOfWeek.minusDays(1))) {
                scoreSemaine += partie.getScore();
            }

        }

        int moyenneDuree = (partiesTerminees > 0) ? (totalDuree / partiesTerminees) : 0;

        // Màj de user
        if (utilisateur.isPresent()) {
            utilisateur.get().setDateDernierePartie(LocalDate.now());
            utilisateur.get().setNbPartiesJouees(partiesTerminees);
            utilisateur.get().setMeilleurTemps(meilleurTemps);
            utilisateur.get().setMeilleurScore(meilleurScore);
            utilisateur.get().setScoreSemaine(scoreSemaine);
            utilisateurRepository.save(utilisateur.get());
        }

        return new Statistiques(idUser, totalTentatives, partiesTerminees, moyenneDuree, meilleurTemps, pireTemps, listeThemesVus.stream().toList(), scoreSemaine);
    }



}
