package m2.miage.interop.servicejeu.controller;

import m2.miage.interop.servicejeu.dto.EnigmeDTO;
import m2.miage.interop.servicejeu.exception.EnigmeInexistanteException;
import m2.miage.interop.servicejeu.exception.FormatEnigmeInvalideException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeEnigme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnigmeControllerUnitTest {

    @Mock
    private FacadeEnigme facadeEnigme;

    @InjectMocks
    private EnigmeController enigmeController;

    private UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");
    private EnigmeDTO enigmeDTO = mock(EnigmeDTO.class);
    private EnigmeDTO enigmeDTOCree = mock(EnigmeDTO.class);

    @Test
    void creerUneEnigme_Success() throws FormatEnigmeInvalideException {

        when(facadeEnigme.creerUneEnigme(enigmeDTO)).thenReturn(enigmeDTOCree);
        when(enigmeDTOCree.getId()).thenReturn(1L);

        ResponseEntity<EnigmeDTO> response = enigmeController.creerUneEnigme(enigmeDTO, uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(enigmeDTOCree, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().endsWith("/api/jeu/enigmes/1"));

        verify(facadeEnigme, times(1)).creerUneEnigme(any(EnigmeDTO.class));
    }

    @Test
    void creerUneEnigme_FormatInvalide() throws FormatEnigmeInvalideException {

        doThrow(FormatEnigmeInvalideException.class).when(facadeEnigme).creerUneEnigme(any(EnigmeDTO.class));

        ResponseEntity<EnigmeDTO> response = enigmeController.creerUneEnigme(enigmeDTO, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).creerUneEnigme(any(EnigmeDTO.class));
    }


    @Test
    void modifierUneEnigme_Success() throws FormatEnigmeInvalideException, EnigmeInexistanteException {
        when(facadeEnigme.modifierUneEnigme(anyLong(), any(EnigmeDTO.class))).thenReturn(enigmeDTOCree);

        ResponseEntity<EnigmeDTO> response = enigmeController.modifierUneEnigme(1L, enigmeDTO, uriBuilder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enigmeDTOCree, response.getBody());

        verify(facadeEnigme, times(1)).modifierUneEnigme(anyLong(), any(EnigmeDTO.class));
    }

    @Test
    void modifierUneEnigme_FormatInvalide() throws FormatEnigmeInvalideException, EnigmeInexistanteException {
        when(facadeEnigme.modifierUneEnigme(anyLong(), any(EnigmeDTO.class))).thenThrow(FormatEnigmeInvalideException.class);

        ResponseEntity<EnigmeDTO> response = enigmeController.modifierUneEnigme(1L, enigmeDTO, uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).modifierUneEnigme(anyLong(), any(EnigmeDTO.class));
    }

    @Test
    void modifierUneEnigme_EnigmeInexistante() throws FormatEnigmeInvalideException, EnigmeInexistanteException {
        when(facadeEnigme.modifierUneEnigme(anyLong(), any(EnigmeDTO.class))).thenThrow(EnigmeInexistanteException.class);

        ResponseEntity<EnigmeDTO> response = enigmeController.modifierUneEnigme(1L, enigmeDTO, uriBuilder);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).modifierUneEnigme(anyLong(), any(EnigmeDTO.class));
    }

    @Test
    void getAllEnigmes_Success() {
        List<EnigmeDTO> enigmes = Arrays.asList(enigmeDTOCree, enigmeDTO);
        when(facadeEnigme.voirToutesLesEnigmes()).thenReturn(enigmes);

        ResponseEntity<List<EnigmeDTO>> response = enigmeController.getAllEnigmes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enigmes, response.getBody());
        assertEquals(2, response.getBody().size());

        verify(facadeEnigme, times(1)).voirToutesLesEnigmes();
    }

    @Test
    void supprimerUneEnigme_Success() throws EnigmeInexistanteException {
        doNothing().when(facadeEnigme).supprimerUneEnigme(anyLong());
        ResponseEntity<Void> response = enigmeController.supprimerUneEnigme(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).supprimerUneEnigme(anyLong());
    }

    @Test
    void supprimerUneEnigme_EnigmeInexistante() throws EnigmeInexistanteException {
        doThrow(EnigmeInexistanteException.class).when(facadeEnigme).supprimerUneEnigme(anyLong());

        ResponseEntity<Void> response = enigmeController.supprimerUneEnigme(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).supprimerUneEnigme(anyLong());
    }


    @Test
    void getEnigmeById_Success() throws EnigmeInexistanteException {

        when(facadeEnigme.getEnigmeById(anyLong())).thenReturn(enigmeDTOCree);

        ResponseEntity<EnigmeDTO> response = enigmeController.getEnigmeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enigmeDTOCree, response.getBody());

        verify(facadeEnigme, times(1)).getEnigmeById(anyLong());
    }

    @Test
    void getEnigmeById_EnigmeInexistante() throws EnigmeInexistanteException {
        when(facadeEnigme.getEnigmeById(anyLong())).thenThrow(EnigmeInexistanteException.class);

        ResponseEntity<EnigmeDTO> response = enigmeController.getEnigmeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(facadeEnigme, times(1)).getEnigmeById(anyLong());
    }

}
