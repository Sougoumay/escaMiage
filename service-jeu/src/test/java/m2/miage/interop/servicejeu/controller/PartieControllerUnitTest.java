package m2.miage.interop.servicejeu.controller;

import m2.miage.interop.servicejeu.dto.PartieDTO;
import m2.miage.interop.servicejeu.dto.ReponseDTO;
import m2.miage.interop.servicejeu.dto.StatistiqueDTO;
import m2.miage.interop.servicejeu.entity.enums.Etat;
import m2.miage.interop.servicejeu.exception.ActionNonAutoriseException;
import m2.miage.interop.servicejeu.exception.PartieNotFoundException;
import m2.miage.interop.servicejeu.exception.ReponseNotFoundException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadePartie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartieControllerUnitTest {

    @Mock
    private FacadePartie facadePartie;

    @InjectMocks
    private PartieController partieController;

    private long idUtilisateur = 1;

    @Mock
    private Authentication authentication;
    
    private List<ReponseDTO> dtos;

    @BeforeEach
    public void setUp() {
        dtos = new ArrayList<>();

        ReponseDTO newDTO1 = new ReponseDTO(1,24,"question1", "Licence3","facile");
        ReponseDTO newDTO2 = new ReponseDTO(1,37,"question2", "Licence3",null);
        ReponseDTO newDTO3 = new ReponseDTO(1,192,"question3", "Licence3","facile");
        ReponseDTO newDTO4 = new ReponseDTO(1,20,"question4", "Master1",null);
        ReponseDTO newDTO5 = new ReponseDTO(1,1,"question5", "Master1","moyen");
        ReponseDTO newDTO6 = new ReponseDTO(1,200,"question6", "Master2","difficile");
        dtos.add(newDTO1);
        dtos.add(newDTO2);
        dtos.add(newDTO3);
        dtos.add(newDTO4);
        dtos.add(newDTO5);
        dtos.add(newDTO6);
    }

    @Test
    void demarrerPartieTest() {
        UriComponents uriComponents = mock(UriComponents.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);

        when(authentication.getName()).thenReturn("1");
        when(builder.path("/api/jeu/parties/{id}")).thenReturn(builder);
        when(builder.buildAndExpand(1L)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/api/jeu/parties/1"));
        when(facadePartie.creerPartie(idUtilisateur)).thenReturn(dtos);

        ResponseEntity<List<ReponseDTO>> response = partieController
                .demarrerPartie(authentication,builder);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'en-tête Location est bien défini
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getLocation());

        // Vérifier l'appel à facadePartie
        verify(facadePartie, times(1)).creerPartie(1L);
    }

    @Test
    public void annulerPartieTestOK() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 1;
        long idUtilisateur = 1;

        when(authentication.getName()).thenReturn(idUtilisateur+"");
        doNothing().when(facadePartie).quitterPartie(idPartie,idUtilisateur);

        ResponseEntity<String> response = partieController.annulerPartie(idPartie,authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(facadePartie, times(1)).quitterPartie(idPartie,idUtilisateur);

    }

    @Test
    public void annulerPartieTestFailPartieNotFound() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 1;
        long idUtilisateur = 1;

        when(authentication.getName()).thenReturn(idUtilisateur+"");
        doThrow(PartieNotFoundException.class).when(facadePartie).quitterPartie(idPartie,idUtilisateur);
        ResponseEntity<String> response = partieController.annulerPartie(idPartie,authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void annulerPartieTestFailActionNonAutorisé() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 1;
        long idUtilisateur = 1;

        when(authentication.getName()).thenReturn(idUtilisateur+"");
        doThrow(ActionNonAutoriseException.class).when(facadePartie).quitterPartie(idPartie,idUtilisateur);

        ResponseEntity<String> response = partieController.annulerPartie(idPartie,authentication);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void repondreAUnEnigmeTestOkReponseTrue() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1;
        long idEnigme = 1;
        long idUtilisateur = 1;
        String reponse = "réponse correcte";
        ReponseDTO reponseDTO = mock(ReponseDTO.class);

        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer(reponse);

        // Mock du service uniquement, car c'est la seule dépendance du contrôleur
        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idEnigme, reponse)).thenReturn(reponseDTO);

        // Appel de la méthode du contrôleur
        ResponseEntity<ReponseDTO> response = partieController.repondreAUnEnigme(idPartie, idEnigme, userAnswer, authentication);

        // Vérification des résultats
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), reponseDTO);
    }

    @Test
    public void repondreAUnEnigmeTestOkReponseFalse() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1;
        long idEnigme = 1;
        long idUtilisateur = 1;
        String reponse = "réponse correcte";
        ReponseDTO reponseDTO = mock(ReponseDTO.class);

        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer(reponse);

        // Mock du service uniquement, car c'est la seule dépendance du contrôleur
        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idEnigme, reponse)).thenReturn(reponseDTO);

        // Appel de la méthode du contrôleur
        ResponseEntity<ReponseDTO> response = partieController.repondreAUnEnigme(idPartie, idEnigme, userAnswer, authentication);

        // Vérification des résultats
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), reponseDTO);
    }

    @Test
    public void repondreAUnEnigmeTestFailPartieNotFound() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1;
        long idEnigme = 1;
        long idUtilisateur = 1;
        String reponse = "réponse correcte";

        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer(reponse);

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idEnigme, reponse)).thenThrow(PartieNotFoundException.class);
        ResponseEntity<ReponseDTO> response = partieController.repondreAUnEnigme(idPartie, idEnigme, userAnswer, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void repondreAUnEnigmeTestFailReponseNotFound() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1;
        long idEnigme = 1;
        long idUtilisateur = 1;
        String reponse = "réponse correcte";

        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer(reponse);

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idEnigme, reponse)).thenThrow(ReponseNotFoundException.class);
        ResponseEntity<ReponseDTO> response = partieController.repondreAUnEnigme(idPartie, idEnigme, userAnswer, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void repondreAUnEnigmeTestFailActionNonAutorise() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1;
        long idEnigme = 1;
        long idUtilisateur = 1;
        String reponse = "réponse correcte";

        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer(reponse);

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreAUnEnigme(idUtilisateur, idPartie, idEnigme, reponse)).thenThrow(ActionNonAutoriseException.class);
        ResponseEntity<ReponseDTO> response = partieController.repondreAUnEnigme(idPartie, idEnigme, userAnswer, authentication);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }


    @Test
    public void consulterEnigmeTestOK() throws ReponseNotFoundException, PartieNotFoundException, ActionNonAutoriseException {
        long idUtilisateur = 1;
        long idPartie = 1;
        long idEnigme = 1;
        ReponseDTO dto = mock(ReponseDTO.class);

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.consulterEnigme(idUtilisateur,idPartie,idEnigme)).thenReturn(dto);

        ResponseEntity<ReponseDTO> response = partieController.consulterEnigme(idPartie, idEnigme, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void consulterEnigmeTestFailPartieNotFound() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idUtilisateur = 1;
        long idPartie = 1;
        long idEnigme = 1;

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.consulterEnigme(idUtilisateur,idPartie,idEnigme)).thenThrow(PartieNotFoundException.class);
        ResponseEntity<ReponseDTO> response = partieController.consulterEnigme(idPartie, idEnigme, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void consulterEnigmeTestFailReponseNotFound() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idUtilisateur = 1;
        long idPartie = 1;
        long idEnigme = 1;

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.consulterEnigme(idUtilisateur,idPartie,idEnigme)).thenThrow(ReponseNotFoundException.class);
        ResponseEntity<ReponseDTO> response = partieController.consulterEnigme(idPartie, idEnigme, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void consulterEnigmeTestFailActionNonAutorise() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idUtilisateur = 1;
        long idPartie = 1;
        long idEnigme = 1;

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.consulterEnigme(idUtilisateur,idPartie,idEnigme)).thenThrow(ActionNonAutoriseException.class);
        ResponseEntity<ReponseDTO> response = partieController.consulterEnigme(idPartie, idEnigme, authentication);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

    }

    @Test
    void repondreALaQuestionMasterOK() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 1L;
        long idUtilisateur = 123L;
        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer("réponse");
        PartieDTO dto = new PartieDTO();

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreALaQuestionMaster(idUtilisateur, idPartie, userAnswer.answer()))
                .thenReturn(dto);

        ResponseEntity<PartieDTO> response = partieController.repondreALaQuestionMaster(
                idPartie, userAnswer, authentication);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(Objects.requireNonNull(response.getBody()).getEtat(), Etat.TERMINE.toString());
    }

    @Test
    void repondreALaQuestionMasterReponseIncorrecte() throws Exception {
        long idPartie = 1L;
        long idUtilisateur = 123L;
        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer("mauvaise réponse");

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreALaQuestionMaster(idUtilisateur, idPartie, userAnswer.answer()))
                .thenReturn(null);
        
        ResponseEntity<PartieDTO> response = partieController.repondreALaQuestionMaster(
                idPartie, userAnswer, authentication);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(Objects.requireNonNull(response.getBody().getEtat()), Etat.TERMINE.toString());
    }

    @Test
    void repondreALaQuestionMasterTestFailActionNonAutorisee() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1L;
        long idUtilisateur = 123L;
        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer("réponse");

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreALaQuestionMaster(idUtilisateur,idPartie,userAnswer.answer()))
                .thenThrow(new ActionNonAutoriseException("Action non autorisée"));

        ResponseEntity<PartieDTO> response = partieController.repondreALaQuestionMaster(
                idPartie, userAnswer, authentication);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    void repondreALaQuestionMasterTestFailPartieNotFound() throws PartieNotFoundException, ActionNonAutoriseException, ReponseNotFoundException {
        long idPartie = 1L;
        long idUtilisateur = 123L;
        PartieController.UserAnswer userAnswer = new PartieController.UserAnswer("réponse");

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        when(facadePartie.repondreALaQuestionMaster(idUtilisateur,idPartie,userAnswer.answer()))
                .thenThrow(new PartieNotFoundException(idPartie));

        ResponseEntity<PartieDTO> response = partieController.repondreALaQuestionMaster(
                idPartie, userAnswer, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void terminerPartieOK() throws Exception {
        long idPartie = 1L;
        long idUtilisateur = 123L;
        StatistiqueDTO statistiqueDTO = new StatistiqueDTO();

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        doNothing().when(facadePartie).terminerPartie(idUtilisateur,idPartie);
        ResponseEntity<String> response = partieController.terminerPartie(idPartie, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vous avez bien cloturé votre partie", response.getBody());
    }

    @Test
    void terminerPartieTestFailPartieNonTrouvee() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 999L;
        long idUtilisateur = 123L;

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        doThrow(new PartieNotFoundException(idPartie)).when(facadePartie).terminerPartie(idUtilisateur,idPartie);
        ResponseEntity<String> response = partieController.terminerPartie(idPartie, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void terminerPartieTestFailActionNonAutorisee() throws PartieNotFoundException, ActionNonAutoriseException {
        long idPartie = 1L;
        long idUtilisateur = 123L;

        when(authentication.getName()).thenReturn(String.valueOf(idUtilisateur));
        doThrow(new ActionNonAutoriseException("Vous n'êtes pas autoriser à clore cette partie")).when(facadePartie).terminerPartie(idUtilisateur,idPartie);

        ResponseEntity<String> response = partieController.terminerPartie(idPartie, authentication);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

}
