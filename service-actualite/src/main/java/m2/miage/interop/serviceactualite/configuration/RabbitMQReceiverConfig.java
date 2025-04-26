package m2.miage.interop.serviceactualite.configuration;

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
public class RabbitMQReceiverConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQReceiverConfig.class);

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

    //Les fils, attention devrait etre différentes entre les services
    @Bean
    public Queue utilisateurQueue() {
        return new Queue(queues.getFirst(), true);
    }

    @Bean
    public Queue badgeQueue() {
        return new Queue(queues.get(1), true);
    }

    @Bean
    public Queue classementQueue() {
        return new Queue(queues.get(2), true);
    }

    @Bean
    public Queue suppressionUtilisateurQueue() {
        return new Queue(queues.get(3), true);
    }


    // La mise en place routingkey +  queue + exchange
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
    Binding bindingBadge() {
        logger.info("le beau badge {}",utilisateurQueue().getActualName());
        return BindingBuilder
                .bind(badgeQueue())
                .to(myExchange())
                .with(routingKeys.get(1))
                .noargs();
    }

    @Bean
    Binding bindingClassement() {
        logger.info("le beau classement {}",utilisateurQueue().getActualName());
        return BindingBuilder
                .bind(classementQueue())
                .to(myExchange())
                .with(routingKeys.get(2))
                .noargs();
    }

    @Bean
    Binding bindingSuppresionUtilisateur() {
        logger.info("la belle suppression utilisateur {}",suppressionUtilisateurQueue().getActualName());
        return BindingBuilder
                .bind(suppressionUtilisateurQueue())
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
