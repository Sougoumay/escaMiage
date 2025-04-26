package m2.miage.interop.serviceauthentification.controller;

import m2.miage.interop.serviceauthentification.config.TypeToken;
import m2.miage.interop.serviceauthentification.controleur.UtilisateurControleur;
import m2.miage.interop.serviceauthentification.dto.LoginDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurCreationDTO;
import m2.miage.interop.serviceauthentification.dto.UtilisateurDTO;
import m2.miage.interop.serviceauthentification.exceptions.FormatInvalideException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurDejaExistantException;
import m2.miage.interop.serviceauthentification.exceptions.UtilisateurInexistantException;
import m2.miage.interop.serviceauthentification.service.FacadeUtilisateur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilisateurControleurUnitTest {

    @InjectMocks
    private UtilisateurControleur utilisateurControleur;

    @Mock
    private FacadeUtilisateur facadeUtilisateur;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BiFunction<UtilisateurDTO, TypeToken ,String> genereToken;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private UriComponentsBuilder uriBuilder;

    @Test
    public void inscrire_AvecBindingErrors_RetourneBadRequest() throws FormatInvalideException, UtilisateurDejaExistantException {
        UtilisateurCreationDTO user = mock(UtilisateurCreationDTO.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.inscrire(user, bindingResult, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeUtilisateur, never()).inscrireUtilisateur(user);
    }

    @Test
    public void inscrire_AvecDonneesValides_RetourneCreated() throws FormatInvalideException, UtilisateurDejaExistantException {
        UtilisateurCreationDTO userCreation = mock(UtilisateurCreationDTO.class);

        long id = 1L;
        UtilisateurDTO userResponse = mock(UtilisateurDTO.class);
        String encodedPassword = "encodedPassword";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(userCreation.password())).thenReturn(encodedPassword);
        when(userResponse.getId()).thenReturn(id);

        ArgumentCaptor<UtilisateurCreationDTO> userCaptor = ArgumentCaptor.forClass(UtilisateurCreationDTO.class);
        UtilisateurCreationDTO capturedUser = userCaptor.capture();
        when(facadeUtilisateur.inscrireUtilisateur(capturedUser)).thenReturn(userResponse);

        UriComponents uriComponents = mock(UriComponents.class);
        when(uriBuilder.path("/escamiage/utilisateur/{idUtilisateur}")).thenReturn(uriBuilder);
        when(uriBuilder.buildAndExpand(id)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/escamiage/utilisateur/1"));

        when(genereToken.apply(userResponse, TypeToken.ACCESS_TOKEN)).thenReturn("jwt-token");

        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.inscrire(userCreation, bindingResult, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        assertEquals("Bearer jwt-token", response.getHeaders().getFirst("Authorization"));

        verify(facadeUtilisateur).inscrireUtilisateur(userCaptor.capture());
    }

    @Test
    public void inscrire_FormatInvalide_RetourneBadRequest() throws FormatInvalideException, UtilisateurDejaExistantException {
        UtilisateurCreationDTO userCreation = mock(UtilisateurCreationDTO.class);

        String encodedPassword = "encodedPassword";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(userCreation.password())).thenReturn(encodedPassword);

        ArgumentCaptor<UtilisateurCreationDTO> userCaptor = ArgumentCaptor.forClass(UtilisateurCreationDTO.class);
        when(facadeUtilisateur.inscrireUtilisateur(userCaptor.capture())).thenThrow(UtilisateurDejaExistantException.class);


        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.inscrire(userCreation, bindingResult, uriBuilder);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(facadeUtilisateur).inscrireUtilisateur(userCaptor.capture());

    }

    @Test
    public void connexion_AvecBindingErrors_RetourneBadRequest() throws FormatInvalideException {
        LoginDTO loginDTO = mock(LoginDTO.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Long> response = utilisateurControleur.connexion(loginDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeUtilisateur, never()).connexionUtilisateur(any());
    }

    @Test
    public void connexion_AvecPaswordIncorrect_RetourneUnAuthorized() throws FormatInvalideException {
        LoginDTO loginDTO = mock(LoginDTO.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(facadeUtilisateur.connexionUtilisateur(loginDTO)).thenThrow(FormatInvalideException.class);

        ResponseEntity<Long> response = utilisateurControleur.connexion(loginDTO, bindingResult);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(facadeUtilisateur).connexionUtilisateur(loginDTO);
    }

    @Test
    public void connexion_AvecDonneesValides_RetourneOk() throws FormatInvalideException {
        LoginDTO loginDTO = mock(LoginDTO.class);
        UtilisateurDTO userDTO = mock(UtilisateurDTO.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(facadeUtilisateur.connexionUtilisateur(loginDTO)).thenReturn(userDTO);
        when(genereToken.apply(userDTO, TypeToken.ACCESS_TOKEN)).thenReturn("jwt-token");
        when(userDTO.getId()).thenReturn(1L);

        ResponseEntity<Long> response = utilisateurControleur.connexion(loginDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody());
        assertEquals("Bearer jwt-token", response.getHeaders().getFirst("Authorization"));
        verify(facadeUtilisateur).connexionUtilisateur(loginDTO);
    }

    @Test
    public void recupererTousLesUtilisateurs_RetourneTousLesUtilisateurs() {
        List<UtilisateurDTO> users = Arrays.asList(
                mock(UtilisateurDTO.class),
                mock(UtilisateurDTO.class)
        );
        when(facadeUtilisateur.getTousLesUtilisateurs()).thenReturn(users);

        ResponseEntity<List<UtilisateurDTO>> response = utilisateurControleur.recupererTousLesUtilisateurs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(facadeUtilisateur).getTousLesUtilisateurs();
    }

    @Test
    public void creerAdmin_AvecBindingErrors_RetourneBadRequest() throws FormatInvalideException, UtilisateurDejaExistantException {
        UtilisateurCreationDTO user = mock(UtilisateurCreationDTO.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.creerUnAdmin(user, bindingResult, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeUtilisateur, never()).creerUnAdmin(user);
    }

    @Test
    public void creerAdmin_AvecDonneesValides_RetourneCreated() throws FormatInvalideException, UtilisateurDejaExistantException {

        UtilisateurCreationDTO userCreation = mock(UtilisateurCreationDTO.class);

        long id = 1L;
        UtilisateurDTO userResponse = mock(UtilisateurDTO.class);
        String encodedPassword = "encodedPassword";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(userCreation.password())).thenReturn(encodedPassword);
        when(userResponse.getId()).thenReturn(id);

        ArgumentCaptor<UtilisateurCreationDTO> userCaptor = ArgumentCaptor.forClass(UtilisateurCreationDTO.class);
        UtilisateurCreationDTO capturedUser = userCaptor.capture();
        when(facadeUtilisateur.creerUnAdmin(capturedUser)).thenReturn(userResponse);

        UriComponents uriComponents = mock(UriComponents.class);
        when(uriBuilder.path("/escamiage/utilisateur/{idUtilisateur}")).thenReturn(uriBuilder);
        when(uriBuilder.buildAndExpand(id)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/escamiage/utilisateur/1"));

        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.creerUnAdmin(userCreation, bindingResult, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());

        verify(facadeUtilisateur).creerUnAdmin(userCaptor.capture());
    }

    @Test
    public void creerAdmin_FormatInvalide_RetourneBadRequest() throws FormatInvalideException, UtilisateurDejaExistantException {

        UtilisateurCreationDTO userCreation = mock(UtilisateurCreationDTO.class);
        String encodedPassword = "encodedPassword";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(userCreation.password())).thenReturn(encodedPassword);

        ArgumentCaptor<UtilisateurCreationDTO> userCaptor = ArgumentCaptor.forClass(UtilisateurCreationDTO.class);
        when(facadeUtilisateur.creerUnAdmin(userCaptor.capture())).thenThrow(UtilisateurDejaExistantException.class);


        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.creerUnAdmin(userCreation, bindingResult, uriBuilder);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(facadeUtilisateur).creerUnAdmin(userCaptor.capture());

    }

    @Test
    public void recupererMonProfil_RetourneProfilUtilisateur() {
        String idUtilisateur = "1";
        UtilisateurDTO userDTO = mock(UtilisateurDTO.class);

        when(facadeUtilisateur.getUtilisateurById(1L)).thenReturn(userDTO);

        ResponseEntity<UtilisateurDTO> response = utilisateurControleur.recupererMonProfil(idUtilisateur, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(facadeUtilisateur).getUtilisateurById(1L);
    }

    @Test
    public void modifyProfile_AvecBindingErrors_RetourneBadRequest() throws FormatInvalideException {
        String idUtilisateur = "1";
        UtilisateurDTO userUpdate = mock(UtilisateurDTO.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = utilisateurControleur.modifyProfile(idUtilisateur, userUpdate, bindingResult, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(facadeUtilisateur, never()).updateUser(anyLong(), any());
    }

    @Test
    public void modifyProfile_AvecDonneesValides_RetourneOk() throws FormatInvalideException {
        String idUtilisateur = "1";
        UtilisateurDTO userUpdate = mock(UtilisateurDTO.class);
        UtilisateurDTO updatedUser = mock(UtilisateurDTO.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(facadeUtilisateur.updateUser(1L, userUpdate)).thenReturn(updatedUser);

        ResponseEntity<?> response = utilisateurControleur.modifyProfile(idUtilisateur, userUpdate, bindingResult, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(facadeUtilisateur).updateUser(1L, userUpdate);
    }

    @Test
    public void modifyProfile_UtilisateurNonTrouve_RetourneNotFound() throws FormatInvalideException {
        String idUtilisateur = "1";
        UtilisateurDTO userUpdate = mock(UtilisateurDTO.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(facadeUtilisateur.updateUser(1L, userUpdate)).thenThrow(FormatInvalideException.class);

        ResponseEntity<?> response = utilisateurControleur.modifyProfile(idUtilisateur, userUpdate, bindingResult, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(facadeUtilisateur).updateUser(1L, userUpdate);
    }

    @Test
    public void supprimerUnUtilisateur_RetourneNoContent() {
        String idUtilisateur = "1";
        doNothing().when(facadeUtilisateur).supprimerUtilisateur(1L);

        ResponseEntity<String> response = utilisateurControleur.supprimerUnUtilisateur(idUtilisateur, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeUtilisateur).supprimerUtilisateur(1L);
    }

    @Test
    public void modifierMotDePasse_RetourneNoContent() {
        String idUtilisateur = "1";
        UtilisateurControleur.MotDePasse motDePasse = new UtilisateurControleur.MotDePasse("nouveauMotDePasse","mail@gmail.com");
        doNothing().when(facadeUtilisateur).modifierMotDePasse(anyLong(), anyString());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ResponseEntity<String> response = utilisateurControleur.modifierMotDePasse(idUtilisateur, motDePasse, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeUtilisateur).modifierMotDePasse(1L, "encodedPassword");
    }

    @Test
    public void genererCodeUnitTest() {
        String email = "email@net.com";
        UtilisateurControleur.EmailRecord emailRecord = mock(UtilisateurControleur.EmailRecord.class);

        when(emailRecord.email()).thenReturn(email);

        doNothing().when(facadeUtilisateur).genererCode(email);

        ResponseEntity<Void> response = utilisateurControleur.genererCode(emailRecord);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeUtilisateur, times(1)).genererCode(email);
    }

    @Test
    public void genererCodeUnitTestFail() {
        String email = "email@net.com";
        UtilisateurControleur.EmailRecord emailRecord = mock(UtilisateurControleur.EmailRecord.class);

        when(emailRecord.email()).thenReturn(email);

        doThrow(UtilisateurInexistantException.class).when(facadeUtilisateur).genererCode(email);

        assertThrows(UtilisateurInexistantException.class, () -> utilisateurControleur.genererCode(emailRecord));
        verify(facadeUtilisateur, times(1)).genererCode(email);
    }

    @Test
    public void verifierCodeUnitTestResultTrue() {
        String email = "email@net.com";
        long code = 12873L;
        UtilisateurControleur.Token token = mock(UtilisateurControleur.Token.class);

        when(token.code()).thenReturn(code);
        when(token.email()).thenReturn(email);

        when(facadeUtilisateur.verifierCode(code,email)).thenReturn(true);

        ResponseEntity<Boolean> response = utilisateurControleur.verifierCode(token);

        assertTrue(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeUtilisateur, times(1)).verifierCode(code,email);
    }

    @Test
    public void verifierCodeUnitTestResultFalse() {
        String email = "email@net.com";
        long code = 12873L;
        UtilisateurControleur.Token token = mock(UtilisateurControleur.Token.class);

        when(token.code()).thenReturn(code);
        when(token.email()).thenReturn(email);

        when(facadeUtilisateur.verifierCode(code,email)).thenReturn(false);

        ResponseEntity<Boolean> response = utilisateurControleur.verifierCode(token);

        assertFalse(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(facadeUtilisateur, times(1)).verifierCode(code,email);
    }
}