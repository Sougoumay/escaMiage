package m2.miage.interop.serviceauthentification.dao;

import m2.miage.interop.serviceauthentification.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {


    boolean existsUtilisateurByEmail(String email);

    Optional<Utilisateur> findByEmail(String email);
}
