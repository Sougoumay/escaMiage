package m2.miage.interop.servicerecompense.dao;

import m2.miage.interop.servicerecompense.modele.BadgeUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeUtilisateurRepository extends JpaRepository<BadgeUtilisateur, Long> {
    Optional<BadgeUtilisateur> findByUtilisateurIdAndBadgeId(long utilisateurId, long badgeId);

    List<BadgeUtilisateur> findByUtilisateurId(long idUtilisateur);
}

