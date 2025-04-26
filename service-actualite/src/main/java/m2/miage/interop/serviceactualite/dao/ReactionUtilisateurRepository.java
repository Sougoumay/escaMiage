package m2.miage.interop.serviceactualite.dao;

import m2.miage.interop.serviceactualite.modele.ReactionUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionUtilisateurRepository extends JpaRepository<ReactionUtilisateur, Long>  {
}
