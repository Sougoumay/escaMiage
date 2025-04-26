package m2.miage.interop.servicerecompense.controleur;

import m2.miage.interop.servicerecompense.dto.UtilisateurDTO;
import m2.miage.interop.servicerecompense.modele.Utilisateur;
import m2.miage.interop.servicerecompense.service.facade.FacadeGestionClassement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassementControleurUnitTest {

    @Mock
    private FacadeGestionClassement facadeGestionClassement;

    @InjectMocks
    private ClassementControleur classementControleur;

    List<UtilisateurDTO> utilisateurs = List.of(mock(UtilisateurDTO.class), mock(UtilisateurDTO.class), mock(UtilisateurDTO.class));

    @Test
    public void recupererClassementGlobalOK() {
        when(facadeGestionClassement.recupererClassementGlobal()).thenReturn(utilisateurs);
        ResponseEntity<List<UtilisateurDTO>> response = classementControleur.recupererClassementGlobal();

        verify(facadeGestionClassement).recupererClassementGlobal();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurs, response.getBody());
    }

    @Test
    public void recupererClassementHebdoOK() {
        when(facadeGestionClassement.recupererClassementHebdo(50)).thenReturn(utilisateurs);
        ResponseEntity<List<UtilisateurDTO>> response = classementControleur.recupererClassementHebdo();

        verify(facadeGestionClassement).recupererClassementHebdo(50);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurs, response.getBody());
    }
}
