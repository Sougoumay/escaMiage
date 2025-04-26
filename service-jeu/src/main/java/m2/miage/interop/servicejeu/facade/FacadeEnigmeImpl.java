package m2.miage.interop.servicejeu.facade;

import m2.miage.interop.servicejeu.dao.EnigmeRepository;
import m2.miage.interop.servicejeu.dto.EnigmeDTO;
import m2.miage.interop.servicejeu.entity.Enigme;
import m2.miage.interop.servicejeu.entity.enums.Difficulte;
import m2.miage.interop.servicejeu.entity.enums.Theme;
import m2.miage.interop.servicejeu.exception.EnigmeInexistanteException;
import m2.miage.interop.servicejeu.exception.FormatEnigmeInvalideException;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeEnigme;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacadeEnigmeImpl implements FacadeEnigme {

    public final EnigmeRepository enigmeRepository;

    public FacadeEnigmeImpl(EnigmeRepository enigmeRepository) {
        this.enigmeRepository = enigmeRepository;
    }

    @Override
    public EnigmeDTO creerUneEnigme(EnigmeDTO enigmeDTO) throws FormatEnigmeInvalideException {
        verificationEnigme(enigmeDTO);
        Enigme enigme =  new Enigme();
        enigme.setQuestion(enigmeDTO.getQuestion());
        enigme.setReponse(enigmeDTO.getReponse());
        enigme.setIndice(enigmeDTO.getIndice());
        enigme.setDifficulte(getDifficulteEnigme(enigmeDTO.getDifficulte()));
        enigme.setTheme(getThemeEnigme(enigmeDTO.getTheme()));
        Enigme enigmeEnregistre = enigmeRepository.save(enigme);
        return new EnigmeDTO(enigmeEnregistre.getId(),
                enigmeEnregistre.getQuestion(),
                enigmeEnregistre.getReponse(),
                enigmeEnregistre.getDifficulte().toString(),
                enigmeEnregistre.getIndice(),
                enigmeEnregistre.getTheme().toString());
    }

    private void verificationEnigme(EnigmeDTO enigmeDTO) throws FormatEnigmeInvalideException {
        if(Objects.isNull(enigmeDTO.getQuestion()) || Objects.isNull(enigmeDTO.getReponse()) || Objects.isNull(enigmeDTO.getIndice()) || Objects.isNull(enigmeDTO.getDifficulte()) || Objects.equals(enigmeDTO.getQuestion(), "")
        || Objects.equals(enigmeDTO.getReponse(), "") || Objects.equals(enigmeDTO.getIndice(), "") || Objects.equals(enigmeDTO.getDifficulte(), "")){
            throw new FormatEnigmeInvalideException();
        }
    }

    @Override
    public EnigmeDTO modifierUneEnigme(long idEnigme, EnigmeDTO enigmeDTO) throws EnigmeInexistanteException, FormatEnigmeInvalideException {
        Enigme enigme = enigmeRepository.findById(idEnigme)
                .orElseThrow(EnigmeInexistanteException::new);
        verificationEnigme(enigmeDTO);
        enigme.setQuestion(enigmeDTO.getQuestion());
        enigme.setReponse(enigmeDTO.getReponse());
        enigme.setDifficulte(getDifficulteEnigme(enigmeDTO.getDifficulte()));
        enigme.setIndice(enigmeDTO.getIndice());
        enigme.setTheme(getThemeEnigme(enigmeDTO.getTheme()));
        Enigme enigmeEnregistre = enigmeRepository.save(enigme);
        return new EnigmeDTO(enigmeEnregistre.getId(),
                enigmeEnregistre.getQuestion(),
                enigmeEnregistre.getReponse(),
                enigmeEnregistre.getDifficulte().toString(),
                enigmeEnregistre.getIndice(),
                enigmeEnregistre.getTheme().toString());
    }


    //int page, int size
    @Override
    public List<EnigmeDTO> voirToutesLesEnigmes() {
        //Pageable pageable = PageRequest.of(page, size);
        List<Enigme> enigmes = enigmeRepository.findAll();
        return enigmes.stream()
                .map(enigme -> new EnigmeDTO(
                        enigme.getId(),
                        enigme.getQuestion(),
                        enigme.getReponse(),
                        enigme.getDifficulte().toString(),
                        enigme.getIndice(),
                        enigme.getTheme().toString()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public void supprimerUneEnigme(long idEnigme) throws EnigmeInexistanteException {
        Enigme enigme = enigmeRepository.findById(idEnigme)
                .orElseThrow(EnigmeInexistanteException::new);
        enigmeRepository.delete(enigme);
    }

    public Difficulte getDifficulteEnigme(String difficulte) throws IllegalArgumentException{
        return Difficulte.valueOf(difficulte);
    }

    private Theme getThemeEnigme(String theme) {
        return Theme.valueOf(theme);
    }

    @Override
    public EnigmeDTO getEnigmeById(long idEnigme) throws EnigmeInexistanteException {
        Optional<Enigme> optionalEnigme = enigmeRepository.findById(idEnigme);
        if (optionalEnigme.isPresent()) {
            Enigme enigme= optionalEnigme.get();
            return new EnigmeDTO(
                    enigme.getId(),
                    enigme.getQuestion(),
                    enigme.getReponse(),
                    enigme.getDifficulte().toString(),
                    enigme.getIndice(),
                    enigme.getTheme().toString());
        } else {
            throw new EnigmeInexistanteException("Aucune énigme trouvée pour l'id : " + idEnigme);
        }
    }
}
