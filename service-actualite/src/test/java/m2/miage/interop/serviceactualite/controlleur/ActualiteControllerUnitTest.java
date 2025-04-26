package m2.miage.interop.serviceactualite.controlleur;

import m2.miage.interop.serviceactualite.dto.PostDTO;
import m2.miage.interop.serviceactualite.dto.ReactionUtilisateurDTO;
import m2.miage.interop.serviceactualite.exceptions.ContenuPostInvalideException;
import m2.miage.interop.serviceactualite.exceptions.PostInexistantException;
import m2.miage.interop.serviceactualite.exceptions.ReactionUtilisateurIncorrectException;
import m2.miage.interop.serviceactualite.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceactualite.service.facade.FacadeActualite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActualiteControllerUnitTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UriComponentsBuilder builder;

    @Mock
    private FacadeActualite facadeActualite;

    @InjectMocks
    private ActualiteController actualiteController;

    List<PostDTO> posts;

    @BeforeEach
    public void setUp() {
        posts = new ArrayList<>();
        PostDTO postDTO1 = mock(PostDTO.class);
        PostDTO postDTO2 = mock(PostDTO.class);
        PostDTO postDTO3 = mock(PostDTO.class);
        posts.add(postDTO1);
        posts.add(postDTO2);
        posts.add(postDTO3);
    }

    @Test
    public void recupererLesPostsTestOK() {
        long idUtilisteur = 1L;

        when(authentication.getName()).thenReturn("1");

        when(facadeActualite.recupererMesPosts(idUtilisteur)).thenReturn(posts);

        ResponseEntity<List<PostDTO>> response = actualiteController.recupererMesPosts(authentication);

        verify(facadeActualite, times(1)).recupererMesPosts(idUtilisteur);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }


    @Test
    void reagirAUnPostTestOK() throws UtilisateurInexistantException, PostInexistantException, ReactionUtilisateurIncorrectException {
        long idPost = 1L;
        long idUtilisateur = 1L;
        ReactionUtilisateurDTO reactionDTO = mock(ReactionUtilisateurDTO.class);
        when(authentication.getName()).thenReturn("1");
        doNothing().when(facadeActualite).reagirAUnPost(idPost,idUtilisateur,reactionDTO);

        ResponseEntity<PostDTO> response = actualiteController.reagirAUnPost(idPost, reactionDTO, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facadeActualite).reagirAUnPost(idPost,idUtilisateur,reactionDTO);

    }

    @Test
    void reagirAUnPostTestFailBadReactionUtilisateurIncorrectException() throws Exception {
        long idPost = 1L;
        long idUtilisateur = 1L;
        ReactionUtilisateurDTO reactionDTO = mock(ReactionUtilisateurDTO.class);
        when(authentication.getName()).thenReturn("1");

        doThrow(ReactionUtilisateurIncorrectException.class)
                .when(facadeActualite).reagirAUnPost(idPost,idUtilisateur,reactionDTO);

        ResponseEntity<PostDTO> response = actualiteController.reagirAUnPost(idPost, reactionDTO, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void reagirAUnPostTestFailBadUtilisateurInexistantException() throws Exception {
        long idPost = 1L;
        long idUtilisateur = 1L;
        ReactionUtilisateurDTO reactionDTO = mock(ReactionUtilisateurDTO.class);
        when(authentication.getName()).thenReturn("1");

        doThrow(UtilisateurInexistantException.class)
                .when(facadeActualite).reagirAUnPost(idPost,idUtilisateur,reactionDTO);

        ResponseEntity<PostDTO> response = actualiteController.reagirAUnPost(idPost, reactionDTO, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void reagirAUnPostTestFailPostInexistantException() throws Exception {
        long idPost = 1L;
        long idUtilisateur = 1L;
        ReactionUtilisateurDTO reactionDTO = mock(ReactionUtilisateurDTO.class);
        when(authentication.getName()).thenReturn("1");

        doThrow(PostInexistantException.class)
                .when(facadeActualite).reagirAUnPost(idPost,idUtilisateur,reactionDTO);

        ResponseEntity<PostDTO> response = actualiteController.reagirAUnPost(idPost, reactionDTO, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void modifierUnPostOK() throws PostInexistantException {
        int idPost = 1;
        PostDTO postDTO = mock(PostDTO.class);

        doNothing().when(facadeActualite).modifierUnPost(idPost,postDTO);

        ResponseEntity<String> response = actualiteController.modifierUnPost(idPost, postDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facadeActualite).modifierUnPost(idPost, postDTO);
    }

    @Test
    void modifierUnPostPostInexistantException() throws PostInexistantException {
        int idPost = 1;
        PostDTO postDTO = mock(PostDTO.class);

        doThrow(PostInexistantException.class).when(facadeActualite).modifierUnPost(idPost, postDTO);

        // Act
        ResponseEntity<String> response = actualiteController.modifierUnPost(idPost, postDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(facadeActualite).modifierUnPost(idPost, postDTO);
    }


    @Test
    void posterUnPostOK() throws Exception {

        PostDTO postDTO = mock(PostDTO.class);
        PostDTO postCree = mock(PostDTO.class);
        long idPost = 23;
        UriComponents uriComponents = mock(UriComponents.class);
        long idUtilisateur = 1L;
        when(authentication.getName()).thenReturn("1");
        when(facadeActualite.ajouterPost(postDTO, idUtilisateur)).thenReturn(postCree);
        when(postCree.getIdPost()).thenReturn(idPost);

        when(builder.path("/escamiage/actualite/{id}")).thenReturn(builder);
        when(builder.buildAndExpand(idPost)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/escamiage/actualite/23"));

        ResponseEntity<PostDTO> response = actualiteController.posterUnPost(postDTO, builder, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postCree, response.getBody());
        verify(facadeActualite).ajouterPost(postDTO, 1L);
    }

    @Test
    void posterUnPostContenuPostInvalideException() throws Exception {
        PostDTO postDTO = mock(PostDTO.class);
        long idUtilisateur = 1L;

        when(authentication.getName()).thenReturn("1");
        doThrow(ContenuPostInvalideException.class).when(facadeActualite).ajouterPost(postDTO, idUtilisateur);

        ResponseEntity<PostDTO> response = actualiteController.posterUnPost(postDTO, builder, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeActualite).ajouterPost(postDTO, 1L);
    }

    @Test
    void posterUnPostContenuUtilisateurInexistantException() throws Exception {
        PostDTO postDTO = mock(PostDTO.class);
        long idUtilisateur = 1L;

        when(authentication.getName()).thenReturn("1");
        doThrow(UtilisateurInexistantException.class).when(facadeActualite).ajouterPost(postDTO, idUtilisateur);

        ResponseEntity<PostDTO> response = actualiteController.posterUnPost(postDTO, builder, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeActualite).ajouterPost(postDTO, 1L);
    }

    @Test
    void supprimerUnPostOK() throws PostInexistantException {
        int idPost = 1;
        doNothing().when(facadeActualite).supprimerUnPost(idPost);
        ResponseEntity<String> response = actualiteController.supprimerUnPost(idPost);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeActualite).supprimerUnPost(idPost);
    }

    @Test
    void supprimerUnPostPostInexistantException() throws PostInexistantException {
        int idPost = 999;
        doThrow(PostInexistantException.class)
                .when(facadeActualite).supprimerUnPost(idPost);

        ResponseEntity<String> response = actualiteController.supprimerUnPost(idPost);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(facadeActualite).supprimerUnPost(idPost);
    }

    @Test
    public void getPostUnitTestOK() throws PostInexistantException {
        int idPost = 12;
        PostDTO postDTO = mock(PostDTO.class);

        when(facadeActualite.recupererUnPost(idPost)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = actualiteController.getUnPost(idPost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
        verify(facadeActualite).recupererUnPost(idPost);
    }

    @Test
    public void getPostUnitTestFailPostInexistantException() throws PostInexistantException {
        int idPost = 999;
        when(facadeActualite.recupererUnPost(idPost)).thenThrow(PostInexistantException.class);
        ResponseEntity<PostDTO> response = actualiteController.getUnPost(idPost);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(facadeActualite).recupererUnPost(idPost);
    }
}
