package m2.miage.interop.servicejeu.facade.interfaces;


import m2.miage.interop.servicejeu.dto.PartieDTO;
import m2.miage.interop.servicejeu.dto.ReponseDTO;
import m2.miage.interop.servicejeu.dto.StatistiqueDTO;
import m2.miage.interop.servicejeu.dto.UtilisateurDTO;
import m2.miage.interop.servicejeu.exception.ActionNonAutoriseException;
import m2.miage.interop.servicejeu.exception.PartieNotFoundException;
import m2.miage.interop.servicejeu.exception.ReponseNotFoundException;

import java.util.List;

public interface FacadePartie {

    List<ReponseDTO> creerPartie(long idUtilisateur);

    void quitterPartie(long partieId, long idUtilisateur) throws PartieNotFoundException, ActionNonAutoriseException;

    ReponseDTO repondreAUnEnigme(long idUtilisateur, long idPartie ,long idEnigme, String reponse) throws ReponseNotFoundException, ActionNonAutoriseException, PartieNotFoundException;

    ReponseDTO consulterEnigme(long idUtilisateur, long idPartie, long idEnigme) throws ReponseNotFoundException, PartieNotFoundException, ActionNonAutoriseException;

    String consulterIndice(long idUtilisateur, long idPartie, long idEnigme) throws ReponseNotFoundException, PartieNotFoundException, ActionNonAutoriseException;

    PartieDTO repondreALaQuestionMaster(long idUtilisateur, long idPartie, String reponse) throws PartieNotFoundException, ActionNonAutoriseException;

    void terminerPartie(long idUtilisateur, long idPartie) throws PartieNotFoundException, ActionNonAutoriseException;

    PartieDTO recupererPartie(long idPartie, long idUtilisateur) throws PartieNotFoundException, ActionNonAutoriseException;

    void supprimerUtilisateur(UtilisateurDTO utilisateur);

    int getMasterIndice(long idPartie) throws PartieNotFoundException;

    StatistiqueDTO recupererStatistiquesPartie(long idUtilisateur, long idPartie) throws ActionNonAutoriseException, PartieNotFoundException;
}
