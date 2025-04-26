package m2.miage.interop.serviceauthentification.service;

import m2.miage.interop.serviceauthentification.dao.UtilisateurRepository;
import m2.miage.interop.serviceauthentification.dao.VerificationCodeRepository;
import m2.miage.interop.serviceauthentification.dto.LoginDTO;
import m2.miage.interop.serviceauthentification.dto.ModifierPassword;
import m2.miage.interop.serviceauthentification.dto.UtilisateurCreationDTO;
import m2.miage.interop.serviceauthentification.modele.Utilisateur;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import m2.miage.interop.serviceauthentification.exceptions.FormatInvalideException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurDejaExistantException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceauthentification.modele.VerificationCode;
import m2.miage.interop.serviceauthentification.modele.enumeration.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacadeUtilisateurImpl implements FacadeUtilisateur {

    private final UtilisateurRepository utilisateurRepository;

    private final VerificationCodeRepository verificationCodeRepository;

    private final PasswordEncoder passwordEncoder;

    private final RabbitMqSender rabbitMqSender;

   //CONSTRUCTEURS

    public FacadeUtilisateurImpl(UtilisateurRepository utilisateurRepository, VerificationCodeRepository verificationCodeRepository, PasswordEncoder passwordEncoder, RabbitMqSender rabbitMqSender) {
        this.utilisateurRepository = utilisateurRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitMqSender = rabbitMqSender;
    }

    @Override
    public UtilisateurDTO inscrireUtilisateur(UtilisateurCreationDTO user)  throws FormatInvalideException, UtilisateurDejaExistantException {
        //verif si user existant
        Optional<Utilisateur> utilisateurExistant = utilisateurRepository.findByEmail(user.email());
        if (utilisateurExistant.isPresent()) {
            throw new UtilisateurDejaExistantException();
        }

        //Vérifications
        // Date de naissance normalement géré par le DTO
        if(estDateNaissanceInvalide(user.dateNaissance())) {
            throw new FormatInvalideException();
        }
        //création du user
        Utilisateur utilisateur = new Utilisateur(
                user.email(),
                user.password(),
                user.nom(),
                user.prenom(),
                user.dateNaissance(),
                user.pseudo(),
                new byte[0]
        );

        Utilisateur utilisateurInscrit = utilisateurRepository.save(utilisateur);
        rabbitMqSender.ajouterUtilisateur(toUtilisateurDTO(utilisateurInscrit));
        return toUtilisateurDTO(utilisateurInscrit);
    }

    @Override
    public UtilisateurDTO connexionUtilisateur(LoginDTO loginDTO) throws UtilisateurInexistantException, FormatInvalideException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(loginDTO.email()).orElseThrow(UtilisateurInexistantException::new);

        if (!passwordEncoder.matches(loginDTO.password(), utilisateur.getPassword())) {
            throw  new FormatInvalideException();
        }
        //long id,String email, String nom, String prenom, LocalDate dateNaissance, String image, String role
        return toUtilisateurDTO(utilisateur);
    }


    @Override
    public List<UtilisateurDTO> getTousLesUtilisateurs() {
        List<Utilisateur> listeUsers = utilisateurRepository.findAll();
        return listeUsers.stream().map(this::toUtilisateurDTO).collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO getUtilisateurById(long idUtilisateur) throws UtilisateurInexistantException {
        return toUtilisateurDTO(utilisateurRepository.findById(idUtilisateur).orElseThrow(UtilisateurInexistantException::new));
    }

    // Le password n'est pas encore géré et on ne peut pas changer son email mettre en place qqch de plus spécifique
    @Override
    public UtilisateurDTO updateUser(long idUtilisateur, UtilisateurDTO utilisateurUpdate) throws FormatInvalideException {

        if(estDateNaissanceInvalide(utilisateurUpdate.getDateNaissance())){
            throw new FormatInvalideException();
        }
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(UtilisateurInexistantException::new);

        //Voir si d'autre champ a mettre à jour
        utilisateur.setNom(utilisateurUpdate.getNom());
        utilisateur.setPrenom(utilisateurUpdate.getPrenom());
        //utilisateur.setEmail(utilisateurUpdate.email());
        utilisateur.setDateNaissance(utilisateurUpdate.getDateNaissance());
        utilisateur.setImage(utilisateurUpdate.getImage());

        return toUtilisateurDTO(utilisateurRepository.save(utilisateur));

    }

    @Transactional
    @Override
    public void modifierMotDePasse(long idUtilisateur,String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(UtilisateurInexistantException::new);
        utilisateur.setPassword(motDePasse);
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    @Override
    public void modifierMotDePasse(String email,String motDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(UtilisateurInexistantException::new);
        utilisateur.setPassword(motDePasse);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(UtilisateurInexistantException::new);
        utilisateurRepository.delete(utilisateur);
        rabbitMqSender.supprimerUtilisateur(new UtilisateurDTO(utilisateur.getId()));
    }

    @Override
    public UtilisateurDTO creerUnAdmin(UtilisateurCreationDTO user) throws UtilisateurDejaExistantException, FormatInvalideException {
        UtilisateurDTO utilisateurDTO = inscrireUtilisateur(user);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurDTO.getEmail()).orElseThrow(UtilisateurInexistantException::new);
        utilisateur.setRole(Role.ADMIN);
        return toUtilisateurDTO(utilisateur);
    }



    @Transactional
    @Override
    public void genererCode(String email) throws UtilisateurInexistantException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(UtilisateurInexistantException::new);


        long code = (long) (Math.random()*1000000);

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setCode(code);
        verificationCode.setExpirationDate(LocalDateTime.now().plusDays(15));
        verificationCode.setUsed(false);
        verificationCode.setEmail(email);

        verificationCodeRepository.save(verificationCode);

        ModifierPassword modifierPassword = new ModifierPassword(utilisateur.getId(), code);
        rabbitMqSender.modifierPassword(modifierPassword);


    }

    @Override
    public boolean verifierCode(long code, String email) {

        VerificationCode verificationCode = verificationCodeRepository.findByCodeAndEmail(code, email);
        if (verificationCode == null || verificationCode.isUsed() || verificationCode.getExpirationDate().isAfter(LocalDateTime.now())) {
            return false;
        }

        verificationCode.setUsed(true);

        return true;
    }


    // A caler dans utilisateur UTILS
    public UtilisateurDTO toUtilisateurDTO (Utilisateur user){
        return new UtilisateurDTO(
                user.getId(),
                user.getEmail(),
                user.getPseudo(),
                user.getNom(),
                user.getPrenom(),
                user.getImage(),
                user.getDateNaissance(),
                user.getRole().toString());
    }

    public boolean estDateNaissanceInvalide(LocalDate date) {
        try {
            //vérif que la date n'est pas dans le futur
            if (date.isAfter(LocalDate.now())) {
                return true;
            }
            //demande âge minimal 18 ans
            return !date.isBefore(LocalDate.now().minusYears(18));

        } catch (Exception e) {
            //si format pas bon ou erreur
            return true;
        }
    }

}
