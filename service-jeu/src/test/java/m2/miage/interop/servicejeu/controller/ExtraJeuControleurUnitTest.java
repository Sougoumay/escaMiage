package m2.miage.interop.servicejeu.controller;

import m2.miage.interop.servicejeu.dto.FeedBackDTO;
import m2.miage.interop.servicejeu.dto.ReponseDTO;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeExtraJeu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ExtraJeuControleurUnitTest {

    @Mock
    private FacadeExtraJeu facadeExtraJeu;

    @InjectMocks
    private ExtraJeuController extraJeuController;

    @Test
    public void donnerFeedBackUnitTest()
    {
        UriComponents uriComponents = mock(UriComponents.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        Authentication authentication = mock(Authentication.class);
        FeedBackDTO feedBackDTO = mock(FeedBackDTO.class);
        long idUtilisateur = 1L;

        when(feedBackDTO.getId()).thenReturn(idUtilisateur);
        when(authentication.getName()).thenReturn("1");
        when(builder.path("/api/jeu/feedback/{idFeedback}")).thenReturn(builder);
        when(builder.buildAndExpand(idUtilisateur)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/api/jeu/feedback/1"));

        when(facadeExtraJeu.donnerFeedBack(idUtilisateur, feedBackDTO)).thenReturn(feedBackDTO);

        ResponseEntity<FeedBackDTO> response = extraJeuController
                .donnerFeedBack(feedBackDTO,authentication,builder);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Vérifier que l'en-tête Location est bien défini
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers.getLocation());

        // Vérifier l'appel à facadePartie
        verify(facadeExtraJeu, times(1)).donnerFeedBack(idUtilisateur, feedBackDTO);
    }
}
