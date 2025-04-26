package m2.miage.interop.serviceactualite.dao;

import m2.miage.interop.serviceactualite.modele.TemplatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplatePostRepository extends JpaRepository<TemplatePost, Long> {
    Optional<TemplatePost> findByTypeBadge(String conditionType);
}
