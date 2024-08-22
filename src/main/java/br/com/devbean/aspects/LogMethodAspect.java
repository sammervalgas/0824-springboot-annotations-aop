package br.com.devbean.aspects;

import br.com.devbean.annotations.LogMethod;
import br.com.devbean.constants.LogMethodLevel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component
public class LogMethodAspect {

    @Around("@annotation(logMethod)")
    public Object intercept(ProceedingJoinPoint joinPoint, LogMethod logMethod) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String messageMask = buildMessage(method);

        if (logMethod.level().equals(LogMethodLevel.DEBUG)) {
            log.debug(messageMask);

        } else if (logMethod.level().equals(LogMethodLevel.ERROR)) {
            log.error(messageMask);

        } else if (logMethod.level().equals(LogMethodLevel.WARN)) {
            log.warn(messageMask);

        } else {
            log.info(messageMask);
        }

        return joinPoint.proceed();
    }

    private static String buildMessage(Method method) {
        StringBuilder messageMask = new StringBuilder()
                .append("====> LOGGING METHOD: [ ")
                .append(method.getDeclaringClass().getSimpleName())
                .append(" ")
                .append(method.getName())
                .append("(");
        if(method.getParameterCount() > 0) {
            Parameter parameter = method.getParameters()[0];
            messageMask
                    .append(parameter.getType().getSimpleName())
                    .append(" ")
                    .append(parameter.getName());
        }
        messageMask.append(") ] ");
        return messageMask.toString();
    }
}
