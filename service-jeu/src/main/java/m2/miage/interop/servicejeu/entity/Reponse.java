package m2.miage.interop.servicejeu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Reponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Partie partie;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Enigme enigme;

    private int nombreTentative;

    private float score;

    private boolean repondu;

    public Reponse() {
    }

    public Reponse(Partie partie, int nombreTentative, Enigme enigme, float score, boolean repondu) {
        this.partie = partie;
        this.nombreTentative = nombreTentative;
        this.enigme = enigme;
        this.score = score;
        this.repondu = repondu;
    }
}
