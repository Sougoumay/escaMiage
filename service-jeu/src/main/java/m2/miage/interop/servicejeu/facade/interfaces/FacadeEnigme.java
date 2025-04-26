package m2.miage.interop.servicejeu.facade.interfaces;

import m2.miage.interop.servicejeu.dto.EnigmeDTO;
import m2.miage.interop.servicejeu.exception.EnigmeInexistanteException;
import m2.miage.interop.servicejeu.exception.FormatEnigmeInvalideException;

import java.util.List;

public interface FacadeEnigme {

    EnigmeDTO creerUneEnigme(EnigmeDTO enigmeDTO) throws FormatEnigmeInvalideException;

    EnigmeDTO modifierUneEnigme(long idEnigme, EnigmeDTO enigme) throws EnigmeInexistanteException, FormatEnigmeInvalideException;

    //Page<EnigmeDTO> voirToutesLesEnigmes(int page, int size);
    List<EnigmeDTO> voirToutesLesEnigmes();

    public void supprimerUneEnigme(long idEnigme) throws EnigmeInexistanteException;


    EnigmeDTO getEnigmeById(long idEnigme) throws EnigmeInexistanteException;

}
