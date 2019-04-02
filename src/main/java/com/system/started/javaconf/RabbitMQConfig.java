package com.system.started.javaconf;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.system.started.javaconf.properties.RabbitMQConfigProperties;

@Configuration
public class RabbitMQConfig {

    @Bean
    @ConfigurationProperties(prefix = "oss.rabbit.apigate.notification")
    public RabbitMQConfigProperties notificationProperties() {
    	RabbitMQConfigProperties notificationProperties = new RabbitMQConfigProperties();
        return notificationProperties;
    }

    @Bean
    public Binding bindingExchangeMessage(RabbitMQConfigProperties notificationProperties) {
        return BindingBuilder.bind(notificationQueue(notificationProperties)).to(notificationExchange(notificationProperties)).with(notificationProperties.getRoutekey());
    }

    @Bean
    public Queue notificationQueue(RabbitMQConfigProperties notificationProperties) {
        return new Queue(notificationProperties.getQueuename(),false,false,true);
    }

    @Bean
    public TopicExchange notificationExchange(RabbitMQConfigProperties notificationProperties) {
    	TopicExchange topicExchange = new TopicExchange(notificationProperties.getTopicExchange(),false,false);
        return topicExchange;
    }

    @Bean  
    public SimpleMessageListenerContainer notificationMessageContainer(CachingConnectionFactory rabbitConnectionFactory,Queue notificationQueue,MessageListener ossNotificationMQClient) {  
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConnectionFactory);  
        container.setQueues(notificationQueue);  
        container.setExposeListenerChannel(true);  
        container.setMaxConcurrentConsumers(1);  
        container.setConcurrentConsumers(1);  
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认  
        container.setMessageListener(ossNotificationMQClient);  
        return container;  
    }  

}
