package m2.miage.interop.servicerecompense.dao;

import m2.miage.interop.servicerecompense.modele.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {

    @Query("SELECT p FROM Partie p LEFT JOIN FETCH p.themes WHERE p.idUtilisateur = :idUser")
    List<Partie> findByIdUtilisateur(@Param("idUser") long idUser);

    public void deleteAllByIdUtilisateur(long idUtilisateur);

}
