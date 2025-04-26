package m2.miage.interop.serviceauthentification.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import m2.miage.interop.serviceauthentification.modele.enumeration.Role;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Utilisateur {

    @Id
    @GeneratedValue()
    private long id;


    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nom;

    private String prenom;

    private LocalDate dateNaissance;

    private String pseudo;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Lob
    private byte[] image;

    //Le rôle par défaut
    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.JOUEUR;
        }
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = new Timestamp(System.currentTimeMillis());
    }


    public Utilisateur(String email, String password, String nom, String prenom, LocalDate dateNaissance, String pseudo, byte[] image) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.pseudo = pseudo;
        this.image = image;
    }

    public Utilisateur() {
    }

}
