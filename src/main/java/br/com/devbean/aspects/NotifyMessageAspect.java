package br.com.devbean.aspects;

import br.com.devbean.annotations.NotifyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Log4j2
@Aspect
@Component
public class NotifyMessageAspect {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public NotifyMessageAspect(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    @AfterReturning(value = "@annotation(notifyMessage)", returning = "result")
    public void notifyMessaging (
            JoinPoint joinPoint,
            NotifyMessage notifyMessage,
            Object result) throws Throwable {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            log.info("===> Running: m=({}) q=({})", method.getName(), notifyMessage.queue());

            if(result instanceof Optional) {
                if(((Optional<?>) result).isPresent()) {
                    result = ((Optional<?>) result).get();
                }
            }

            if (notifyMessage.enabled()) {
                rabbitTemplate.convertAndSend(
                        notifyMessage.queue(),
                        mapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            log.error("===> Error under notify event message: {}", e.getMessage());
        }
        log.info("===> Finished Process");

    }


}
