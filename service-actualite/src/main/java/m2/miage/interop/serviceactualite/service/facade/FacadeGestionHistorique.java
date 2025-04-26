package m2.miage.interop.serviceactualite.service.facade;

import m2.miage.interop.serviceactualite.dto.UtilisateurDTO;

public interface FacadeGestionHistorique {

    void ajouterUtilisateur(UtilisateurDTO utilisateur);
   void supprimerUtilsateur(UtilisateurDTO utilisateur);
}
