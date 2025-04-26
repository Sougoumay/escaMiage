package m2.miage.interop.servicerecompense.service;

import jakarta.transaction.Transactional;
import m2.miage.interop.servicerecompense.dao.UtilisateurRepository;
import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.modele.Utilisateur;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionClassement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacadeGestionClassementImpl implements FacadeGestionClassement {

    private final UtilisateurRepository utilisateurRepository;

    public FacadeGestionClassementImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public List<UtilisateurDTO> recupererClassementGlobal(){
        List<Utilisateur> utilisateurs = utilisateurRepository.findAllByOrderByMeilleurScoreDescNbPartiesJoueesDesc();

        if (utilisateurs.isEmpty()) {
            return new ArrayList<>();
        }

        return utilisateurs.stream()
                .map(utilisateur -> new UtilisateurDTO(
                        utilisateur.getId(),
                        utilisateur.getMeilleurScore(),
                        utilisateur.getMeilleurTemps(),
                        utilisateur.getDateDernierePartie(),
                        utilisateur.getNbPartiesJouees(),
                        utilisateur.getScoreSemaine()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurDTO> recupererClassementHebdo(int topSouhaite){
        Pageable pageable = PageRequest.of(0, topSouhaite);
        List<Utilisateur> utilisateurs = utilisateurRepository.findAllByOrderByScoreSemaineDescNbPartiesJoueesDesc(pageable);
        if (utilisateurs.isEmpty()) {
            return new ArrayList<>();
        }

        return utilisateurs.stream()
                .map(utilisateur -> new UtilisateurDTO(
                        utilisateur.getId(),
                        utilisateur.getMeilleurScore(),
                        utilisateur.getMeilleurTemps(),
                        utilisateur.getDateDernierePartie(),
                        utilisateur.getNbPartiesJouees(),
                        utilisateur.getScoreSemaine()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void resetScoresHebdomadaires() {
        utilisateurRepository.resetScoresHebdomadaires();
    }
}
