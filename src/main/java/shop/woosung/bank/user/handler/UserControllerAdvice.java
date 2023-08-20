package shop.woosung.bank.user.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.user.handler.exception.EmailAlreadyInUseException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicatedEmailException(HttpServletRequest request, EmailAlreadyInUseException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("EmailAlreadyInUseException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("이미 사용중인 이메일 입니다."));
    }
}
