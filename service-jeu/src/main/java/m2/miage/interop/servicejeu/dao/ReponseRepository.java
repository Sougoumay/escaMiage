package m2.miage.interop.servicejeu.dao;

import m2.miage.interop.servicejeu.entity.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
}
