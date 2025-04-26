package m2.miage.interop.serviceactualite.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.serviceactualite.modele.enums.TypePost;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String contenu;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datePublication;

    @ManyToOne
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private TypePost type; // AUTO ou MANUEL

    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReactionUtilisateur> reactions =  new ArrayList<>();;

    @PrePersist
    protected void onCreate() {
        this.datePublication = LocalDateTime.now();
    }

    public Post(String contenu, Utilisateur auteur, TypePost type) {
        this.contenu = contenu;
        this.utilisateur = auteur;
        this.type = type;
    }
    public Post(String contenu, Utilisateur auteur, TypePost type, byte[] image) {
        this.contenu = contenu;
        this.utilisateur = auteur;
        this.type = type;
        this.image = image;
    }

    public Post() {
    }
}
