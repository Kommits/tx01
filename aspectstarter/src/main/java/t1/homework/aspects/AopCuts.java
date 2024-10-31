package t1.homework.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AopCuts {
    private static final Logger logger = LoggerFactory.getLogger(AopCuts.class);
    private Boolean logging;
    private Integer level;

    public AopCuts(Boolean _logging, Integer _level){
        logging = _logging; level = _level;
    }

    // тут такой момент:
    // ничего умнее как думать о том - что "перехватить хттп" - это значит перехват
    // метода с аннотацией или гет или пост
    // мне в голову честно говоря не приходит
    // история еще есть с тем чтобы вообще перехватывать все что спринг шлет и получает
    // через хттп, ну дз про стартеры, а не про то как хттп эксчендж работает, поэтому пусть будет так
    // в моем кейсе потому что он хттп перехватывает отлично
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingMethods() {}
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingMethods() {}

    // я уж не буду выносить в отдельный сервис само логгирование, чтобы структура была очевидней
    @Around("getMappingMethods() || postMappingMethods()")
    public Object logAroundGet(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var annotations = method.getAnnotations();
        if(logging) {
            if(level > 0) logger.info("Invoking method: {} with arguments: {}", joinPoint.getSignature(), methodArgs);
            if(level > 1) logger.info("Method: {} has annotations: {}", method.getName(), Arrays.toString(annotations));
        }
        Object result = joinPoint.proceed();
        if(logging) {
            if(level > 2) logger.info("Method: {} returned: {}", joinPoint.getSignature(), result);
        }
        return result;
    }
}
