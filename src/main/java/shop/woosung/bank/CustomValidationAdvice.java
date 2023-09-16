package shop.woosung.bank;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class CustomValidationAdvice {

    public final String POST_MAPPING_ANNOTATION = "@annotation(org.springframework.web.bind.annotation.PostMapping)";
    public final String PUT_MAPPING_ANNOTATION = "@annotation(org.springframework.web.bind.annotation.PutMapping)";

    @Pointcut(POST_MAPPING_ANNOTATION)
    public void postMapping() {}

    @Pointcut(PUT_MAPPING_ANNOTATION)
    public void putMapping() {}

    @Around("postMapping() || putMapping()")
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) {
                    Map<String, String> errorMap = bindingResult.getFieldErrors().stream()
                            .peek(error -> log.error("error: {}", error.getDefaultMessage()))
                            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                    throw new RuntimeException("유효성 검사 실패");
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
