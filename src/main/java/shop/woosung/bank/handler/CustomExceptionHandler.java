package shop.woosung.bank.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.woosung.bank.dto.ResponseDto;
import shop.woosung.bank.handler.ex.CustomApiException;

/*
    @ExceptionHandler같은 경우는 @Controller, @RestController가 적용된 Bean내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 기능을 한다.
@ExceptionHandler가 하나의 클래스에 대한 것이라면, @ControllerAdvice는 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation이다.
@RestControllerAdvice와 @ControllerAdvice가 존재하는데 @RestControllerAdvice 어노테이션을 들여다보면 아래와 같이 되어있다.
@ControllerAdvice와 동일한 역할 즉, 예외를 잡아 핸들링 할 수 있도록 하는 기능을 수행하면서 @ResponseBody를 통해 객체를 리턴할 수도 있다는 얘기다.

ViewResolver를 통해서 예외 처리 페이지로 리다이렉트 시키려면 @ControllerAdvice만 써도 되고, API서버여서 에러 응답으로 객체를 리턴해야한다면 @ResponseBody 어노테이션이 추가된 @RestControllerAdvice를 적용하면 되는 것이다.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomApiException.class) // 커스텀API예외가 터지면 이 메서드가 실행됨
    public ResponseEntity<?> apiException(CustomApiException e) { // ?부분 이 부분 조금더 개념 보충 필요
        log.error(e.getMessage());

        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

}
