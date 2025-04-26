package m2.miage.interop.servicejeu.dao;

import m2.miage.interop.servicejeu.entity.Enigme;
import m2.miage.interop.servicejeu.entity.enums.Difficulte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnigmeRepository extends JpaRepository<Enigme, Long> {
    //List<Enigme> findByDifficulte(Difficulte difficulte);
}
