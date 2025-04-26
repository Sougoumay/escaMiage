package m2.miage.interop.servicejeu.facade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import m2.miage.interop.servicejeu.dao.PartieRepository;
import m2.miage.interop.servicejeu.dao.ReponseRepository;
import m2.miage.interop.servicejeu.dto.PartieDTO;
import m2.miage.interop.servicejeu.dto.ReponseDTO;
import m2.miage.interop.servicejeu.dto.StatistiqueDTO;
import m2.miage.interop.servicejeu.dto.UtilisateurDTO;
import m2.miage.interop.servicejeu.entity.*;
import m2.miage.interop.servicejeu.entity.enums.Difficulte;
import m2.miage.interop.servicejeu.entity.enums.Etat;
import m2.miage.interop.servicejeu.exception.ActionNonAutoriseException;
import m2.miage.interop.servicejeu.exception.PartieNotFoundException;
import m2.miage.interop.servicejeu.exception.ReponseNotFoundException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadePartie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FacadePartieImpl implements FacadePartie {

    private final PartieRepository partieRepository;
    private final ReponseRepository reponseRepository;
    private final RabbitMqSender rabbitMqSender;

    @PersistenceContext
    private final EntityManager entityManager;

    public FacadePartieImpl(PartieRepository partieRepository, ReponseRepository reponseRepository,  RabbitMqSender rabbitMqSender, EntityManager entityManager) {
        this.partieRepository = partieRepository;
        this.reponseRepository = reponseRepository;
        this.rabbitMqSender = rabbitMqSender;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public List<ReponseDTO> creerPartie(long idUtilisateur) {
        long tempAlloue = 30;
        Partie partie = new Partie();
        partie.setUtilisateur(idUtilisateur);
        partie.setTempsAlloue(tempAlloue * 60);
        partie = partieRepository.save(partie);

        List<Reponse> reponses = createReponsesForPartie(partie);
        String codePartieEtIndice = definirCodePartie(reponses);

        char indiceChar = codePartieEtIndice.charAt(codePartieEtIndice.length() - 1); // Dernier caractère
        int indice = Character.getNumericValue(indiceChar);
        String codePartie = codePartieEtIndice.substring(0, codePartieEtIndice.length() - 1); // Tous les caractères sauf le dernier
        partie.setIndiceCode(indice);
        partie.setCode(codePartie);
        partieRepository.save(partie);

        List<ReponseDTO> reponseDTOS = new ArrayList<>();
        reponses.forEach(reponse -> {
            Enigme enigme = reponse.getEnigme();
            reponseDTOS.add(new ReponseDTO(
                    reponse.getPartie().getId(),
                    reponse.getId(),
                    enigme.getQuestion(),
                    enigme.getDifficulte().name(),
                    enigme.getIndice()));
        });
        return reponseDTOS;
    }


    @Transactional
    @Override
    public void quitterPartie(long partieId, long idUtilisateur) throws PartieNotFoundException, ActionNonAutoriseException {
        Partie partie = partieRepository.findById(partieId).orElseThrow(() -> new PartieNotFoundException(partieId));

        if (partie.getUtilisateur()!= idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à annuler cette parte");
        }

        LocalDateTime now = LocalDateTime.now();
        double scoreFinal = calculerScoreFinal(partie,now);
        partie.setScoreFinal(scoreFinal);
        partie.setEtat(Etat.ANNULE);
        partie.setTempsFinal(now);

        partieRepository.save(partie);
    }

    @Transactional
    @Override
    public ReponseDTO repondreAUnEnigme(long idUtilisateur, long idPartie, long idEnigme, String reponse) throws ReponseNotFoundException, ActionNonAutoriseException, PartieNotFoundException {
        Reponse response = reponseRepository.findById(idEnigme).orElseThrow(() -> new ReponseNotFoundException(idEnigme));


        if (response.getPartie().getId() != idPartie) {
            throw new PartieNotFoundException(idPartie);
        }

        if (response.getPartie().getUtilisateur()!= idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à répondre à cet enigme");
        }

        if (!response.getPartie().getEtat().equals(Etat.ENCOURS)) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à répondre à cet enigme");
        }

        boolean result = getTreatedAnswer(reponse).equals(response.getEnigme().getReponse().toLowerCase());
        if (result) {
            response.setRepondu(true);
        } else {
            response.setNombreTentative(response.getNombreTentative()+1);
        }
        reponseRepository.save(response);

        //On renvoie une reponseDTO pour que le front puisse faire de l'affichage dynamique
        ReponseDTO reponseDTO = new ReponseDTO();
        reponseDTO.setRepondu(response.isRepondu());
        reponseDTO.setNombreTentative(response.getNombreTentative());

        if (!response.isRepondu() && response.getNombreTentative() >= 5) {
            reponseDTO.setIndice(response.getEnigme().getIndice());
        }
        return reponseDTO;
    }

    @Override
    public ReponseDTO consulterEnigme(long idUtilisateur, long idPartie, long idEnigme) throws ReponseNotFoundException, PartieNotFoundException, ActionNonAutoriseException {
        Reponse reponse = reponseRepository.findById(idEnigme).orElseThrow(() -> new  ReponseNotFoundException(idEnigme));

        if (reponse.getPartie().getId() != idPartie) {
            throw new PartieNotFoundException(idPartie);
        }

        if (reponse.getPartie().getUtilisateur()!= idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à consulter cet enigme");
        }
        Enigme enigme = reponse.getEnigme();
        return new ReponseDTO(
                reponse.getPartie().getId(),
                reponse.getId(),
                enigme.getQuestion(),
                enigme.getDifficulte().name(),
                reponse.getNombreTentative() >= 5 ? enigme.getIndice() : "",
                reponse.getNombreTentative(),
                reponse.getScore(),
                reponse.isRepondu()
        );
    }

    @Override
    public String consulterIndice(long idUtilisateur, long idPartie, long idEnigme) throws ReponseNotFoundException, PartieNotFoundException, ActionNonAutoriseException {
        Reponse reponse = reponseRepository.findById(idEnigme).orElseThrow(() -> new  ReponseNotFoundException(idEnigme));

        if (reponse.getPartie().getId() != idPartie) {
            throw new PartieNotFoundException(idPartie);
        }

        if (reponse.getPartie().getUtilisateur() != idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à consulter cet indice");
        }

        return reponse.getEnigme().getIndice();
    }

    @Transactional
    @Override
    public PartieDTO repondreALaQuestionMaster(long idUtilisateur, long idPartie, String reponse) throws PartieNotFoundException, ActionNonAutoriseException {

        Partie partie = partieRepository.findById(idPartie).orElseThrow(() -> new PartieNotFoundException(idPartie));
        PartieDTO partieDTO = new PartieDTO();
        partieDTO.setId(partie.getId());
        partieDTO.setDateCreation(partieDTO.getDateCreation());

        if (partie.getUtilisateur() != idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autorisé à effectuer cette action");
        }

        if (partie.getEtat().name().equals(Etat.TERMINE.name())) {
            throw new ActionNonAutoriseException("Vous avez déjà répondu à la master question");
        }

        boolean result = getTreatedAnswer(reponse).equals(partie.getCode().toLowerCase());
        StatistiqueDTO dto = null;

        if (result) {
            double scoreMasterCode = getScoreMasterCode(partie.getNbreTentativeCode());
            LocalDateTime now = LocalDateTime.now();
            double scoreTempsEtQuestion = calculerScoreFinal(partie,now);
            partie.setScoreFinal(scoreMasterCode+scoreTempsEtQuestion);
            partie.setEtat(Etat.TERMINE);
            partie.setTempsFinal(now);
            partieRepository.save(partie);
            dto = getStatistiqueDTO(partie);
            dto.setReussie(true);
            rabbitMqSender.envoyerUnePartie(dto);
        } else {
            partie.setNbreTentativeCode(partie.getNbreTentativeCode()+1);
            partieDTO.setNbreTentativeCode(partie.getNbreTentativeCode());
            partieRepository.save(partie);
        }
        partieDTO.setEtat(partie.getEtat().toString());
        return partieDTO;
    }

    @Transactional
    @Override
    public void terminerPartie(long idUtilisateur, long idPartie) throws PartieNotFoundException, ActionNonAutoriseException {
        Partie partie = partieRepository.findById(idPartie).orElseThrow(() -> new PartieNotFoundException(idPartie));
        if (partie.getUtilisateur() != idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autoriser à clore cette partie");
        }

        if (partie.getEtat().name().equals(Etat.TERMINE.name())) {
            throw new ActionNonAutoriseException("Vous avez déjà terminé cette partie");
        }

        LocalDateTime now = LocalDateTime.now();
        double scoreFinal = calculerScoreFinal(partie,now);
        partie.setScoreFinal(scoreFinal);
        partie.setEtat(Etat.TERMINE);
        partie.setTempsFinal(now);
        partie = partieRepository.save(partie);
        StatistiqueDTO dto = getStatistiqueDTO(partie);
        dto.setReussie(false);
        rabbitMqSender.envoyerUnePartie(dto);
    }

    @Override
    public PartieDTO recupererPartie(long idPartie, long idUtilisateur) throws PartieNotFoundException, ActionNonAutoriseException {
        Partie partie = partieRepository.findById(idPartie).orElseThrow(() -> new PartieNotFoundException(idPartie));
        if (idUtilisateur != partie.getUtilisateur()){
            throw new ActionNonAutoriseException("Vous n'etes pas autorisé à consulter cette partie");
        }
       return new PartieDTO(
               partie.getId(),
               partie.getUtilisateur(),
               partie.getEtat().toString(),
               partie.getScoreFinal(),
               partie.getReponses().stream().map(r -> new ReponseDTO(r.getId(),r.getPartie().getId(),r.getEnigme().getQuestion(),r.isRepondu())).toList(),
               partie.getDateCreation()
       );
    }

    @Transactional
    @Override
    public void supprimerUtilisateur(UtilisateurDTO utilisateur) {
        partieRepository.deleteAllByUtilisateur(utilisateur.getId());
    }

    @Override
    public int getMasterIndice(long idPartie) throws PartieNotFoundException {
        Partie partie = partieRepository.findById(idPartie).orElseThrow(() -> new PartieNotFoundException(idPartie));
        return partie.getIndiceCode();
    }

    @Override
    public StatistiqueDTO recupererStatistiquesPartie(long idUtilisateur, long idPartie) throws ActionNonAutoriseException, PartieNotFoundException {
        Partie partie = partieRepository.findById(idPartie).orElseThrow(() -> new PartieNotFoundException(idPartie));
        if (partie.getUtilisateur() != idUtilisateur) {
            throw new ActionNonAutoriseException("Vous n'êtes pas autoriser à clore cette partie");
        }

        return getStatistiqueDTO(partie);
    }

    private double getScoreMasterCode(int nbreTentativeCode) {
        double scoreMasterCode = 0;
        if (nbreTentativeCode==0) {
            scoreMasterCode = 500;
        } else if(nbreTentativeCode==1) {
            scoreMasterCode = (double) (500 * 90) /100;
        } else if(nbreTentativeCode==2) {
            scoreMasterCode = (double) (500 * 70) /100;
        } else {
            scoreMasterCode = (double) (500 * 50) /100;
        }

        return scoreMasterCode;
    }

    private List<Enigme> getEnigmes() {
        List<Enigme> enigmes = new ArrayList<>();
        enigmes.addAll(getRandomEntitiesByDifficulty(Difficulte.LICENCE3));
        enigmes.addAll(getRandomEntitiesByDifficulty(Difficulte.MASTER1));
        enigmes.addAll(getRandomEntitiesByDifficulty(Difficulte.MASTER2));

        return enigmes;
    }

    private List<Reponse> createReponsesForPartie(Partie partie) {
        List<Enigme> enigmes = getEnigmes();
        List<Reponse> reponses = new ArrayList<>();

        enigmes.forEach(e -> {
            Reponse reponse = new Reponse();
            reponse.setRepondu(false);
            reponse.setEnigme(e);
            reponse.setPartie(partie);
            String difficulte = e.getDifficulte().name();
            float scoreEnigme = difficulte.equals("LICENCE3") ?
                    100 : difficulte.equals("MASTER1") ? 200 : 300;
            reponse.setScore(scoreEnigme);

            reponses.add(reponseRepository.save(reponse));
        });

        return reponses;
    }


    private List<Enigme> getRandomEntitiesByDifficulty(Difficulte difficulte) {
        return entityManager.createQuery("SELECT e FROM Enigme e WHERE e.difficulte = :difficulty ORDER BY RAND() limit 5", Enigme.class)
                .setParameter("difficulty", difficulte)
                .getResultList();
    }

    private StatistiqueDTO getStatistiqueDTO(Partie partie) {

        int nbTentativesTotal = partie.getNbreTentativeCode();

        long dureePartieEnSeconde = Duration.between(partie.getDateCreation(),partie.getTempsFinal()).getSeconds();

        StatistiqueDTO statistiqueDTO = new StatistiqueDTO();
        statistiqueDTO.setId(partie.getId());
        statistiqueDTO.setIdUtilisateur(partie.getUtilisateur());
        statistiqueDTO.setDureePartieEnSecondes(dureePartieEnSeconde);
        statistiqueDTO.setTempsRestant(partie.getTempsAlloue() - dureePartieEnSeconde);

        statistiqueDTO.setScore(partie.getScoreFinal());
        List<String> themes = new ArrayList<>();
        int nbreReponseCorrecte = 0;


        for (Reponse reponse : partie.getReponses()) {
            if (reponse.isRepondu()) {
                nbreReponseCorrecte++;
            }

            Enigme enigme = reponse.getEnigme();
            themes.add(enigme.getTheme().toString());
            nbTentativesTotal += reponse.getNombreTentative();
        }
        statistiqueDTO.setReponsesCorrectes(nbreReponseCorrecte);
        statistiqueDTO.setThemes(themes);
        statistiqueDTO.setNbTentativesTotal(nbTentativesTotal);
        statistiqueDTO.setReussie(true);

        return statistiqueDTO;
    }

    private String getTreatedAnswer(String answer) {
        // Convertir la chaîne en minuscules
        String lowerCaseInput = answer.toLowerCase();

        // Remplacer les caractères accentués par leur version sans accent
        String result = lowerCaseInput
                .replaceAll("[éèêë]", "e") // Remplacer tous les 'é', 'è', 'ê', 'ë' par 'e'
                .replaceAll("[áàâä]", "a") // Remplacer tous les 'á', 'à', 'â', 'ä' par 'a'
                .replaceAll("ç", "c")       // Remplacer 'ç' par 'c'
                .replaceAll("[îï]", "i")    // Remplacer tous les 'î', 'ï' par 'i'
                .replaceAll("[ôö]", "o")    // Remplacer tous les 'ô', 'ö' par 'o'
                .replaceAll("[ùüû]", "u")   // Remplacer tous les 'ù', 'ü', 'û' par 'u'
                .replaceAll("[ÿ]", "y")     // Remplacer 'ÿ' par 'y'
                .replaceAll("[œ]", "oe")    // Remplacer 'œ' par 'oe'
                .replaceAll("[æ]", "ae");   // Remplacer 'æ' par 'ae'

        return result;
    }

    private boolean isAnswerToLessOneQuestion(Partie partie) {
        boolean result = false;
        for (Reponse reponse : partie.getReponses()) {
            if (reponse.isRepondu()) {
                result = true;
                break;
            }
        }

        return result;
    }

    private double calculerScoreFinal(Partie partie, LocalDateTime now) {
        double scoreQuestion = 0;
        double scoreTemps = 0;
        if (isAnswerToLessOneQuestion(partie)) {
            long tempsAlloueSeconde = partie.getTempsAlloue()*60;
            long tempsPartieSeconde = Duration.between(now, partie.getDateCreation()).getSeconds();
            scoreTemps = ((double) (tempsAlloueSeconde - tempsPartieSeconde) / tempsPartieSeconde)*1500;
        }

        for (Reponse reponse : partie.getReponses()) {
            if (reponse.isRepondu()) {
                int nbreTentative = reponse.getNombreTentative();
                double score = reponse.getScore();
                if (nbreTentative==0) {
                    scoreQuestion = scoreQuestion + score;
                } else if(nbreTentative==1) {
                    scoreQuestion = scoreQuestion + score*90/100;
                } else if(nbreTentative==2) {
                    scoreQuestion = scoreQuestion + score*70/100;
                } else {
                    scoreQuestion = scoreQuestion + score*50/100;
                }
            }
        }

        return scoreTemps+scoreQuestion;
    }


    // RELATIF à la création de la réponse à la master question
    private String definirCodePartie(List<Reponse> reponses) {
        StringBuilder codePartieBuilder = new StringBuilder();

        for (Reponse reponse : reponses) {
            String enigmeReponse = reponse.getEnigme().getReponse().toLowerCase();
            codePartieBuilder.append(enigmeReponse.charAt(0));
        }

        Random random = new Random();
        int decalage = random.nextInt(5) + 1;
        return chiffrement(codePartieBuilder.toString(),decalage);
    }

    private String chiffrement(String texte, int decalage) {
        StringBuilder resultat = new StringBuilder();

        for (char c : texte.toCharArray()) {
            if (Character.isLetter(c)) {
                // Si c'est une lettre, on applique le décalage
                char nouvelleLettre = deplaceLettre(c, decalage);
                resultat.append(nouvelleLettre);
            } else if (Character.isDigit(c)) {
                // Si c'est un chiffre, on le transforme en lettre correspondante et on fait le décalage
                char nouvelleLettre = deplaceLettre(chiffreVersLettre(c), decalage);
                resultat.append(nouvelleLettre);
            } else {
                // On garde les caractères non alphanumériques inchangés
                resultat.append(c);
            }
        }

        resultat.append(decalage);
        return resultat.toString();
    }

    private char deplaceLettre(char c, int decalage) {
        return (char) (((c - 'a' + decalage) % 26) + 'a');
    }

    private char chiffreVersLettre(char c) {
        int chiffre = c - '0';  // On obtient la valeur numérique du caractère
        return (char) ('a' + chiffre); // On transforme 0 -> a, 1 -> b, ..., 9 -> j
    }

}
