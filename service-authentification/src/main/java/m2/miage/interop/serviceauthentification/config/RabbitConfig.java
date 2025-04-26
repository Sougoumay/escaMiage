package m2.miage.interop.serviceauthentification.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.List;

@Configuration
public class RabbitConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

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
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).build();
    }

    @Bean
    public Queue BienvenueQueue() {
        return QueueBuilder.durable(queues.getFirst()).build();
    }

    @Bean
    Binding bindingBienvenue() {
        return BindingBuilder
                .bind(BienvenueQueue())
                .to(exchange())
                .with(routingKeys.getFirst())
                .noargs();
    }

    @Bean
    public Queue ModificationPasswordQueue() {
        return QueueBuilder.durable(queues.get(1)).build();
    }

    @Bean
    Binding modificationPasswordBinding() {
        return BindingBuilder
                .bind(BienvenueQueue())
                .to(exchange())
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

