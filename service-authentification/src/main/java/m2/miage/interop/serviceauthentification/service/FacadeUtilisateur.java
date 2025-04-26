package m2.miage.interop.serviceauthentification.service;

import m2.miage.interop.serviceauthentification.dto.LoginDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurCreationDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import m2.miage.interop.serviceauthentification.exceptions.FormatInvalideException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurDejaExistantException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurInexistantException;

import java.util.List;

public interface FacadeUtilisateur {

    //METHODES
    UtilisateurDTO inscrireUtilisateur(UtilisateurCreationDTO user)  throws FormatInvalideException, UtilisateurDejaExistantException;

    UtilisateurDTO connexionUtilisateur(LoginDTO loginDTO) throws UtilisateurInexistantException, FormatInvalideException;

    List<UtilisateurDTO> getTousLesUtilisateurs();

    UtilisateurDTO getUtilisateurById(long id) throws UtilisateurInexistantException;

    UtilisateurDTO updateUser(long idUtilisateur, UtilisateurDTO utilisateurExistant) throws FormatInvalideException;

    void supprimerUtilisateur(long idUtilisateur);

    UtilisateurDTO creerUnAdmin(UtilisateurCreationDTO utilisateurCreationDTO) throws UtilisateurDejaExistantException, FormatInvalideException;

    void modifierMotDePasse(long idUtilisateur,String motDePasse);

    void modifierMotDePasse(String email,String motDePasse);

    void genererCode(String email);

    boolean verifierCode(long code, String email);
}
