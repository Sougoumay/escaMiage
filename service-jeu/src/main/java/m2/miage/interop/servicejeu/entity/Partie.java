package m2.miage.interop.servicejeu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicejeu.entity.enums.Etat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long utilisateur;

    @Enumerated(EnumType.STRING)
    private Etat etat;


    private double scoreFinal;


    private String code;


    private int nbreTentativeCode;


    private int indiceCode;


    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateCreation;

    // en minutes
    private long tempsAlloue;

    private LocalDateTime tempsFinal;

    @OneToMany(mappedBy = "partie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reponse> reponses;

    @PrePersist
    public void onCreate(){
        dateCreation = LocalDateTime.now();
        etat = Etat.ENCOURS;
        nbreTentativeCode = 0;
    }

    public Partie() {
    }
}
