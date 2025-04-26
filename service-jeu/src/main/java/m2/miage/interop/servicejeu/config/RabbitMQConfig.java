package m2.miage.interop.servicejeu.config;

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

    @Value("#{'${spring.rabbitmq.queue}'.split(',')}")
    private List<String> queues;

    @Value("#{'${spring.rabbitmq.routingkey}'.split(',')}")
    private List<String> routingKeys;

    @Value("${spring.rabbitmq.host}")
    String host;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;
    @Bean
    public Exchange myExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }


    @Bean
    public Queue suppressionUtilisateurQueue() {
        return new Queue(queues.getFirst(), true);
    }


    // La mise en place routingkey +  queue + exchange
    @Bean
    Binding bindingUtilisateur() {
        logger.info("le bel utilisateur {}",suppressionUtilisateurQueue().getActualName());
        return BindingBuilder
                .bind(suppressionUtilisateurQueue())
                .to(myExchange())
                .with(routingKeys.get(1))
                .noargs();
    }

    @Bean
    public Queue feedBackQueue() {
        return new Queue(queues.get(1), true);
    }


    // La mise en place routingkey +  queue + exchange
    @Bean
    Binding bindingFeedBack() {
        return BindingBuilder
                .bind(feedBackQueue())
                .to(myExchange())
                .with(routingKeys.get(2))
                .noargs();
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
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
