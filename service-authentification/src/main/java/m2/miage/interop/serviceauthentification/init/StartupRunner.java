package m2.miage.interop.serviceauthentification.init;

import m2.miage.interop.serviceauthentification.dao.UtilisateurRepository;
import m2.miage.interop.serviceauthentification.modele.Utilisateur;
import m2.miage.interop.serviceauthentification.modele.enumeration.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    public final UtilisateurRepository utilisateurRepository;

    public final PasswordEncoder passwordEncoder;

    public StartupRunner(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initAdmin();
        initJoueur();
    }


    private void initAdmin() {
       if (utilisateurRepository.findByEmail("admin@admin.com").isEmpty()){
           Utilisateur admin = new Utilisateur();
           admin.setEmail("admin@admin.com");
           admin.setPassword(passwordEncoder.encode("Admin0101@"));
           admin.setNom("Admin");
           admin.setPrenom("Admin");
           admin.setRole(Role.ADMIN);
           utilisateurRepository.save(admin);
           logger.info("✅ Insertion de l'admin");
        }else {
           logger.info("ℹ️ Admin déjà présent en base.");
       }
    }

    private void initJoueur() {
        if (utilisateurRepository.findByEmail("joueur@joueur.com").isEmpty()){
            Utilisateur joueur = new Utilisateur();
            joueur.setEmail("joueur@joueur.com");
            joueur.setPassword(passwordEncoder.encode("miage"));
            joueur.setNom("miage");
            joueur.setPrenom("miage");
            utilisateurRepository.save(joueur);
            logger.info("✅ Insertion du joueur");
        }else {
            logger.info("ℹ️ Joueur déjà présent en base.");
        }
    }
}
