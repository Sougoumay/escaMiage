package m2.miage.interop.servicejeu.facade.interfaces;

import jakarta.validation.Valid;
import m2.miage.interop.servicejeu.dto.FeedBackDTO;

public interface FacadeExtraJeu {
    FeedBackDTO donnerFeedBack(long idUtilisateur, @Valid FeedBackDTO feedBack);
}
