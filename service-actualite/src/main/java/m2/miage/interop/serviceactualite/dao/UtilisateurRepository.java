package m2.miage.interop.serviceactualite.dao;


import m2.miage.interop.serviceactualite.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    //@Modifying
    //@Query("UPDATE Utilisateur u SET u.scoreSemaine = 0")
    //void resetScoresHebdomadaires();

    //List<Utilisateur> findTop10ByOrderByScoreSemaineDesc();

    Optional<Utilisateur> findByPseudo(String pseudo);
}
