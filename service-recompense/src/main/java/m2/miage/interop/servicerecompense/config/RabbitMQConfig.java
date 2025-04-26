package m2.miage.interop.servicerecompense.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("#{'${spring.rabbitmq.queue}'.split(',')}")
    private List<String> queues;

    @Value("#{'${spring.rabbitmq.routingkey}'.split(',')}")
    private List<String> routingKeys;

    @Bean
    public Exchange myExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    //Lors de la création d'un nouvel utilisateur
    @Bean
    public Queue utilisateurQueue() {
        return new Queue(queues.getFirst(), true);
    }

    //Lors de l'achevement d'une partie
    @Bean
    public Queue partieQueue() {
        return new Queue(queues.get(1), true);
    }

    //lors de la suppression d'un utilisateur
    @Bean
    public Queue suppressionUtilisateurQueue() {
        return new Queue(queues.get(2), true);
    }

    @Bean
    public Queue classementHebdomadaireQueue() {
        return QueueBuilder.durable(queues.get(3)).build();
    }

    @Bean
    Binding bindingUtilisateur() {
        logger.info("le bel utilisateur {}",utilisateurQueue().getActualName());
        return BindingBuilder
                .bind(utilisateurQueue())
                .to(myExchange())
                .with(routingKeys.getFirst())
                .noargs();
    }


    @Bean
    Binding bindingPartie() {
        logger.info("la belle partie {}",partieQueue().getActualName());
        return BindingBuilder
                .bind(partieQueue())
                .to(myExchange())
                .with(routingKeys.get(1))
                .noargs();
    }

    @Bean
    Binding bindingSuppression() {
        logger.info("la belle suppression{}",suppressionUtilisateurQueue().getActualName());
        return BindingBuilder
                .bind(suppressionUtilisateurQueue())
                .to(myExchange())
                .with(routingKeys.get(4))
                .noargs();
    }

    @Bean
    Binding bindingClassementHebdomadaire() {
        return BindingBuilder
                .bind(classementHebdomadaireQueue())
                .to(myExchange())
                .with(routingKeys.get(3))
                .noargs();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
