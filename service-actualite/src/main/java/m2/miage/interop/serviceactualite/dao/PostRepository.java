package m2.miage.interop.serviceactualite.dao;

import m2.miage.interop.serviceactualite.modele.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  PostRepository  extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.type = 'MANUEL' " +
            "OR (p.type = 'AUTO' AND p.utilisateur IS NULL) " +
            "OR (p.type = 'AUTO' AND p.utilisateur.id = :idUtilisateur)")
    List<Post> findMesPosts(long idUtilisateur);
}
