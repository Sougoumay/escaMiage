package m2.miage.interop.servicerecompense.controleur;

import m2.miage.interop.servicerecompense.dto.BadgeDTO;
import m2.miage.interop.servicerecompense.service.exceptions.*;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionBadge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BadgeControleurUnitTest {

    @Mock
    private FacadeGestionBadge facadeGestionBadge;

    @InjectMocks
    private BadgeControleur badgeControleur;

    @Test
    public void recupererBadgeByIdOK() throws PasDeBadgeExistantException {
        String idBadge = "1";

        BadgeDTO dto = mock(BadgeDTO.class);
        when(facadeGestionBadge.recupererBadgeById(1)).thenReturn(dto);

        ResponseEntity<BadgeDTO> response = badgeControleur.recupererBadgeById(idBadge);

        verify(facadeGestionBadge).recupererBadgeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void recupererBadgeByIdThrowNumberFormatException() throws PasDeBadgeExistantException {
        String idBadge = "none";
        ResponseEntity<BadgeDTO> response = badgeControleur.recupererBadgeById(idBadge);
        verify(facadeGestionBadge,never()).recupererBadgeById(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void recupererBadgeByIdThrowPasDeBadgeExistantException() throws PasDeBadgeExistantException {
        String idBadge = "1";
        when(facadeGestionBadge.recupererBadgeById(1)).thenThrow(PasDeBadgeExistantException.class);
        ResponseEntity<BadgeDTO> response = badgeControleur.recupererBadgeById(idBadge);
        verify(facadeGestionBadge).recupererBadgeById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void supprimerBadgeByIdOK() throws PasDeBadgeExistantException {
        String idBadge = "1";
        doNothing().when(facadeGestionBadge).supprimerBadge(1);
        ResponseEntity<Void> response = badgeControleur.supprimerBadgeById(idBadge);
        verify(facadeGestionBadge).supprimerBadge(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void supprimerBadgeByIdThrowNumberFormatException() throws PasDeBadgeExistantException {
        String idBadge = "none";
        ResponseEntity<Void> response = badgeControleur.supprimerBadgeById(idBadge);
        verify(facadeGestionBadge,never()).supprimerBadge(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void supprimerBadgeByIdThrowPasDeBadgeExistantException() throws PasDeBadgeExistantException {
        String idBadge = "1";
        doThrow(PasDeBadgeExistantException.class).when(facadeGestionBadge).supprimerBadge(1);
        ResponseEntity<Void> response = badgeControleur.supprimerBadgeById(idBadge);
        verify(facadeGestionBadge).supprimerBadge(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void recupererTousBadgesOK() {
        List<BadgeDTO> badges = List.of(mock(BadgeDTO.class),mock(BadgeDTO.class),mock(BadgeDTO.class));

        when(facadeGestionBadge.recupererTousBadges()).thenReturn(badges);

        ResponseEntity<List<BadgeDTO>> response = badgeControleur.recupererTousBadges();
        verify(facadeGestionBadge).recupererTousBadges();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(badges, response.getBody());
    }

    @Test
    public void modifierBadgeOK() throws PasDeBadgeExistantException, InformationManquanteException, InformationIncorrecteException {
        String idBadge = "1";
        BadgeDTO dto = mock(BadgeDTO.class);
        when(facadeGestionBadge.modifierBadge(dto,1)).thenReturn(dto);

        ResponseEntity<BadgeDTO> response = badgeControleur.modifierBadge(idBadge,dto);

        verify(facadeGestionBadge).modifierBadge(dto,1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void modifierBadgeThrowNumberFormatException() throws PasDeBadgeExistantException, InformationManquanteException, InformationIncorrecteException {
        String idBadge = "none";
        ResponseEntity<BadgeDTO> response = badgeControleur.modifierBadge(idBadge,null);
        verify(facadeGestionBadge,never()).modifierBadge(null,1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void modifierBadgeThrowInformationManquanteException() throws PasDeBadgeExistantException, InformationManquanteException, InformationIncorrecteException {
        String idBadge = "1";
        BadgeDTO dto = mock(BadgeDTO.class);
        when(facadeGestionBadge.modifierBadge(dto,1)).thenThrow(InformationManquanteException.class);

        ResponseEntity<BadgeDTO> response = badgeControleur.modifierBadge(idBadge,dto);

        verify(facadeGestionBadge).modifierBadge(dto,1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void modifierBadgeThrowInformationIncorrecteException() throws PasDeBadgeExistantException, InformationManquanteException, InformationIncorrecteException {
        String idBadge = "1";
        BadgeDTO dto = mock(BadgeDTO.class);
        when(facadeGestionBadge.modifierBadge(dto,1)).thenThrow(InformationIncorrecteException.class);

        ResponseEntity<BadgeDTO> response = badgeControleur.modifierBadge(idBadge,dto);

        verify(facadeGestionBadge).modifierBadge(dto,1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void modifierBadgeThrowPasDeBadgeExistantException() throws PasDeBadgeExistantException, InformationManquanteException, InformationIncorrecteException {
        String idBadge = "1";
        BadgeDTO dto = mock(BadgeDTO.class);
        when(facadeGestionBadge.modifierBadge(dto,1)).thenThrow(PasDeBadgeExistantException.class);

        ResponseEntity<BadgeDTO> response = badgeControleur.modifierBadge(idBadge,dto);

        verify(facadeGestionBadge).modifierBadge(dto,1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void creerBadgeOK() throws TypeBadgeInexistantException, InformationIncorrecteException, BadgeDejaExistantException {
        long badgeId = 1;
        BadgeDTO dto = mock(BadgeDTO.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        UriComponents uriComponents = mock(UriComponents.class);

        when(dto.getId()).thenReturn(badgeId);
        when(builder.path("/escamiage/recompense/{idBadge}")).thenReturn(builder);
        when(builder.buildAndExpand(badgeId)).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(URI.create("http://localhost/escamiage/recompense/1"));

        when(facadeGestionBadge.creerBadge(dto)).thenReturn(dto);

        ResponseEntity<BadgeDTO> response = badgeControleur.creerBadge(dto,builder);

        verify(facadeGestionBadge).creerBadge(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());

    }

    @Test
    public void creerBadgeThrowTypeBadgeInexistantException() throws TypeBadgeInexistantException, InformationIncorrecteException, BadgeDejaExistantException {
        BadgeDTO dto = mock(BadgeDTO.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        when(facadeGestionBadge.creerBadge(dto)).thenThrow(TypeBadgeInexistantException.class);
        ResponseEntity<BadgeDTO> response = badgeControleur.creerBadge(dto,builder);
        verify(facadeGestionBadge).creerBadge(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void creerBadgeThrowInformationIncorrecteException() throws TypeBadgeInexistantException, InformationIncorrecteException, BadgeDejaExistantException {
        BadgeDTO dto = mock(BadgeDTO.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        when(facadeGestionBadge.creerBadge(dto)).thenThrow(InformationIncorrecteException.class);
        ResponseEntity<BadgeDTO> response = badgeControleur.creerBadge(dto,builder);
        verify(facadeGestionBadge).creerBadge(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void creerBadgeThrowBadgeDejaExistantException() throws TypeBadgeInexistantException, InformationIncorrecteException, BadgeDejaExistantException {
        BadgeDTO dto = mock(BadgeDTO.class);
        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        when(facadeGestionBadge.creerBadge(dto)).thenThrow(BadgeDejaExistantException.class);
        ResponseEntity<BadgeDTO> response = badgeControleur.creerBadge(dto,builder);
        verify(facadeGestionBadge).creerBadge(dto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
