package m2.miage.interop.servicerecompense.dao;

import m2.miage.interop.servicerecompense.modele.Partie;
import m2.miage.interop.servicerecompense.modele.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u ORDER BY u.meilleurScore DESC, u.nbPartiesJouees DESC")
    List<Utilisateur> findAllByOrderByMeilleurScoreDescNbPartiesJoueesDesc();

    @Modifying
    @Query("UPDATE Utilisateur u SET u.scoreSemaine = 0")
    void resetScoresHebdomadaires();

    @Query("SELECT u FROM Utilisateur u ORDER BY u.scoreSemaine DESC, u.nbPartiesJouees DESC")
    List<Utilisateur> findAllByOrderByScoreSemaineDescNbPartiesJoueesDesc(Pageable pageable);


    Optional<Utilisateur> findUtilisateurById(long id);
}
