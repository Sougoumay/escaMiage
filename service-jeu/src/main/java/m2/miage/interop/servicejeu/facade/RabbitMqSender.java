package m2.miage.interop.servicejeu.facade;

import m2.miage.interop.servicejeu.dto.FeedBackDTO;
import m2.miage.interop.servicejeu.dto.StatistiqueDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMqSender {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("#{'${spring.rabbitmq.routingkey}'.split(',')}")
    private List<String> routingKeys;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void envoyerUnePartie(StatistiqueDTO statistiqueDTO){
        rabbitTemplate.convertAndSend(exchange,routingKeys.getFirst(), statistiqueDTO);
    }

    public void sendFeedBack(FeedBackDTO feedBackDTO) {

        rabbitTemplate.convertAndSend(exchange,routingKeys.get(2), feedBackDTO);

    }
}
