package m2.miage.interop.servicerecompense.dao;

import m2.miage.interop.servicerecompense.modele.Badge;
import m2.miage.interop.servicerecompense.modele.BadgeUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    @Query("SELECT b FROM Badge b WHERE b.conditionType = :conditionType AND b.conditionValue = :conditionValue")
    Badge findByConditionTypeAndConditionValue(@Param("conditionType") String conditionType, @Param("conditionValue") String conditionValue);

    //@Query("SELECT b FROM Badge b WHERE b.id = :id")
    Optional<Badge> findBadgeById(long id);

}
