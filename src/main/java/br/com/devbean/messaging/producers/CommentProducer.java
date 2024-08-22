package br.com.devbean.messaging.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentProducer {

    private final RabbitTemplate rabbitTemplate;

    public CommentProducer(
            RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void postComment(String message) {
        this.rabbitTemplate.convertAndSend("", message);
    }
}
