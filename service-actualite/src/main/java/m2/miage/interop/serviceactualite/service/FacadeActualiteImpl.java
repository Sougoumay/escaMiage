package m2.miage.interop.serviceactualite.service;

import m2.miage.interop.serviceactualite.dao.PostRepository;
import m2.miage.interop.serviceactualite.dao.ReactionUtilisateurRepository;
import m2.miage.interop.serviceactualite.dao.UtilisateurRepository;
import m2.miage.interop.serviceactualite.dto.PostDTO;
import m2.miage.interop.serviceactualite.dto.ReactionUtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.ContenuPostInvalideException;
import m2.miage.interop.serviceactualite.exceptions.PostInexistantException;
import m2.miage.interop.serviceactualite.exceptions.ReactionUtilisateurIncorrectException;
import m2.miage.interop.serviceactualite.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceactualite.modele.Post;
import m2.miage.interop.serviceactualite.modele.ReactionUtilisateur;
import m2.miage.interop.serviceactualite.modele.Utilisateur;
import m2.miage.interop.serviceactualite.modele.enums.ReactionPost;
import m2.miage.interop.serviceactualite.modele.enums.TypePost;
import m2.miage.interop.serviceactualite.service.facade.FacadeActualite;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacadeActualiteImpl implements FacadeActualite {

    private final PostRepository postRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ReactionUtilisateurRepository reactionUtilisateurRepository;

    public FacadeActualiteImpl(PostRepository postRepository,UtilisateurRepository utilisateurRepository, ReactionUtilisateurRepository reactionUtilisateurRepository) {
        this.postRepository = postRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.reactionUtilisateurRepository = reactionUtilisateurRepository;
    }

    @Override
    public PostDTO ajouterPost(PostDTO postDTO, long idUtilisateur) throws ContenuPostInvalideException, UtilisateurInexistantException {
        if (Objects.isNull(postDTO.getContenu()) || postDTO.getContenu().trim().isEmpty()) {
            throw new ContenuPostInvalideException();
        }

        Post post = new Post();
        post.setContenu(postDTO.getContenu());
        post.setImage(postDTO.getImagePost());
        post.setType(TypePost.MANUEL);

        Post postEnregistre = postRepository.save(post);

        return new PostDTO(
                postEnregistre.getId(),
                postEnregistre.getContenu(),
                postEnregistre.getImage()
        );
    }


    @Override
    public void reagirAUnPost(long idPost, long idUtilisateur, ReactionUtilisateurDTO reactionDTO) throws ReactionUtilisateurIncorrectException, UtilisateurInexistantException, PostInexistantException {
        if (Objects.isNull(reactionDTO.getTypeReaction())) {
            throw new ReactionUtilisateurIncorrectException("Le type de réaction ne peut pas être nul.");
        }

        // Vérification de l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new UtilisateurInexistantException(reactionDTO.getIdUtilisateur()));

        // Vérification du post
        Post post = postRepository.findById(idPost)
                .orElseThrow(() -> new PostInexistantException("Le post demandé n'existe pas"));

        // Création de la réaction
        ReactionUtilisateur reaction = new ReactionUtilisateur();
        reaction.setReactionPost(getReactionPost(reactionDTO.getTypeReaction()));
        reaction.setUtilisateur(utilisateur);
        reaction.setPost(post);

        // Sauvegarde de la réaction
        reactionUtilisateurRepository.save(reaction);
    }


    @Override
    public void supprimerUnPost(long idPost) throws PostInexistantException {
        postRepository.findById(idPost).orElseThrow(() -> new PostInexistantException("Le post demandé n'existe pas"));
        postRepository.deleteById(idPost);
    }

    @Override
    public void modifierUnPost(long idPost, PostDTO postDTO) throws PostInexistantException {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new PostInexistantException("Le post demandé n'existe pas"));
        post.setContenu(postDTO.getContenu());
        post.setImage(postDTO.getImagePost());
        postRepository.save(post);
    }

    @Override
    public ReactionPost getReactionPost(String reaction) throws IllegalArgumentException{
        return ReactionPost.valueOf(reaction);
    }

    @Override
    public PostDTO recupererUnPost(long idPost) throws PostInexistantException {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new PostInexistantException("Le post demandé n'existe pas"));
        return new PostDTO(
                post.getId(),
                post.getContenu(),
                post.getImage(),
                post
                        .getReactions()
                        .stream()
                        .map(r -> new ReactionUtilisateurDTO(
                                r.getReactionPost().name(),
                                r.getUtilisateur().getId()
                        )).toList()
        );
    }

    @Override
    public List<PostDTO> recupererMesPosts(long idUtilisateur) {
        List<Post> mesPosts = postRepository.findMesPosts(idUtilisateur);
        List<PostDTO> postDTOS = mesPosts.stream().map(p-> new PostDTO(
                p.getId(),
                p.getContenu(),
                p.getImage(),
                p.getReactions().stream().map(r->new ReactionUtilisateurDTO(r.getReactionPost().toString())).toList()))
                .toList();
        return postDTOS;
    }
}
