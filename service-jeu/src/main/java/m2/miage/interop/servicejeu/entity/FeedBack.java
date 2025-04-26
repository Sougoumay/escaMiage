package m2.miage.interop.servicejeu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import m2.miage.interop.servicejeu.entity.enums.SujetFeedBack;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack {
    @Id
    @GeneratedValue
    private Long id;

    private Long idUtilisateur;

    private String message;

    @Enumerated(EnumType.STRING)
    private SujetFeedBack sujetFeedBack;
}
