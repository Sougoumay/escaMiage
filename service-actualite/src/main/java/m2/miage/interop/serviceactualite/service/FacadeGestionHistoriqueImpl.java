package m2.miage.interop.serviceactualite.service;

import m2.miage.interop.serviceactualite.dao.PostRepository;
import m2.miage.interop.serviceactualite.dao.UtilisateurRepository;
import m2.miage.interop.serviceactualite.dto.UtilisateurDTO;
import m2.miage.interop.serviceactualite.modele.Utilisateur;
import m2.miage.interop.serviceactualite.service.facade.FacadeGestionHistorique;
import org.springframework.stereotype.Service;

@Service
public class FacadeGestionHistoriqueImpl implements FacadeGestionHistorique {

    public final UtilisateurRepository utilisateurRepository;
    public final PostRepository repository;

    public FacadeGestionHistoriqueImpl(UtilisateurRepository utilisateurRepository, PostRepository repository) {
        this.utilisateurRepository = utilisateurRepository;
        this.repository = repository;
    }

    @Override
    public void ajouterUtilisateur(UtilisateurDTO utilisateur) {
        utilisateurRepository.save(new Utilisateur(utilisateur.getId(),utilisateur.getPseudo()));
    }

    @Override
    public void supprimerUtilsateur(UtilisateurDTO utilisateur) {
        utilisateurRepository.deleteById(utilisateur.getId()); //va egalement supprimer les posts associés
    }
}
