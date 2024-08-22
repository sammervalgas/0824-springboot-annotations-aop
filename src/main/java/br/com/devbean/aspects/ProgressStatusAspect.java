package br.com.devbean.aspects;

import br.com.devbean.annotations.ProgressStatus;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class ProgressStatusAspect {

    @Before("@annotation(progressStatus)")
    private void initProcess(JoinPoint joinPoint, ProgressStatus progressStatus) {
       log.info("Progress initiation... step: {}", progressStatus.step());
    }

}
