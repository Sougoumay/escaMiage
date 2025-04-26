package m2.miage.interop.servicejeu.dao;

import m2.miage.interop.servicejeu.entity.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {

    void deleteAllByUtilisateur(long utilisateur);
}
