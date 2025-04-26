package m2.miage.interop.servicerecompense.dao;

import m2.miage.interop.servicerecompense.modele.Statistiques;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiquesRepository extends JpaRepository<Statistiques, Long> {
    void deleteAllByIdUtilisateur(long idUtilisateur);
}
