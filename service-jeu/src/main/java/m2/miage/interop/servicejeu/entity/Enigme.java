package m2.miage.interop.servicejeu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.servicejeu.entity.enums.Difficulte;
import m2.miage.interop.servicejeu.entity.enums.Theme;

import java.util.List;

@Getter
@Setter
@Entity
public class Enigme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Column(nullable = false)
    private String question;

    //@Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulte difficulte;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme theme;

    private String reponse;

    private String indice;

    /*@OneToMany(mappedBy = "enigme", fetch = FetchType.LAZY)
    private List<Reponse> reponses;*/

    public Enigme() {
    }

    public Enigme(String question, Difficulte difficulte, Theme theme, String reponse, String indice) {
        this.question = question;
        this.difficulte = difficulte;
        this.theme = theme;
        this.reponse = reponse;
        this.indice = indice;
    }

}
