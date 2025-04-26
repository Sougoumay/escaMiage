package m2.miage.interop.servicejeu.facade;

import m2.miage.interop.servicejeu.dao.FeedBackRepository;
import m2.miage.interop.servicejeu.dto.FeedBackDTO;
import m2.miage.interop.servicejeu.entity.FeedBack;
import m2.miage.interop.servicejeu.entity.enums.SujetFeedBack;
import m2.miage.interop.servicejeu.entity.enums.Theme;
import m2.miage.interop.servicejeu.facade.interfaces.FacadeExtraJeu;
import org.springframework.stereotype.Service;

@Service
public class FacadeExtraJeuImpl implements FacadeExtraJeu {

    private final FeedBackRepository feedBackRepository;
    private final RabbitMqSender rabbitMqSender;

    public FacadeExtraJeuImpl(FeedBackRepository feedBackRepository, RabbitMqSender rabbitMqSender) {
        this.feedBackRepository = feedBackRepository;
        this.rabbitMqSender = rabbitMqSender;
    }

    @Override
    public FeedBackDTO donnerFeedBack(long idUtilisateur, FeedBackDTO feedBack) {
        FeedBack feedBackCree = new FeedBack();
        feedBackCree.setIdUtilisateur(idUtilisateur);
        feedBackCree.setSujetFeedBack(SujetFeedBack.valueOf(feedBack.getSujet()));
        feedBackCree.setMessage(feedBack.getMessage());
        feedBackRepository.save(feedBackCree);

        FeedBackDTO feedBackDTO = new FeedBackDTO(feedBackCree.getId(), feedBack.getMessage(),feedBackCree.getSujetFeedBack().toString());
        FeedBackDTO feedBackDTO2 = feedBackDTO;
        feedBackDTO2.setIdUtilisateur(feedBackCree.getIdUtilisateur());

        rabbitMqSender.sendFeedBack(feedBackDTO2);

        return feedBackDTO;
    }

}
