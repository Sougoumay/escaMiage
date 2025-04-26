package m2.miage.interop.serviceactualite.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur {

    @Id
    private Long id;

    String pseudo;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> mesPosts;

    public Utilisateur() {
    }

    public Utilisateur(long id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }
}
