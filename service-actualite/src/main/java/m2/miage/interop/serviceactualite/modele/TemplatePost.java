package m2.miage.interop.serviceactualite.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TemplatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String typeBadge;

    @Column(columnDefinition = "TEXT")
    private String contenuTemplate; // Stocke le JSON sous forme de texte

    public TemplatePost() {
    }

    public TemplatePost(String typeBadge, String contenuTemplate) {
        this.typeBadge = typeBadge;
        this.contenuTemplate = contenuTemplate;
    }
}

