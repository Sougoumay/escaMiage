package m2.miage.interop.servicerecompense.init;

import m2.miage.interop.servicerecompense.dao.UtilisateurRepository;
import m2.miage.interop.servicerecompense.modele.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JoueurRunner implements CommandLineRunner {

    public final UtilisateurRepository utilisateurRepository;
    private static final Logger logger = LoggerFactory.getLogger(JoueurRunner.class);

    public JoueurRunner(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void run(String... args) {
       initJoueurs();
    }

    public void initJoueurs() {
        if (!utilisateurRepository.existsById(1L)) {
            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.setId(1L);
            utilisateurRepository.save(utilisateur1);
        }

        // Vérification si l'utilisateur avec l'ID 2 existe déjà
        if (!utilisateurRepository.existsById(2L)) {
            Utilisateur utilisateur2 = new Utilisateur();
            utilisateur2.setId(2L);
            utilisateurRepository.save(utilisateur2);
        }
    }
}
