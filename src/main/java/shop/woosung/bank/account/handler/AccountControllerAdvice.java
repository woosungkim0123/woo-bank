package shop.woosung.bank.account.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountSequenceException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumberException;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.user.handler.exception.EmailAlreadyInUseException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class AccountControllerAdvice {

    @ExceptionHandler(NotFoundAccountTypeNumberException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundAccountTypeNumberException(HttpServletRequest request, NotFoundAccountTypeNumberException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("NotFoundAccountTypeNumberException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("잘못된 계좌 종류 입니다."));
    }

    @ExceptionHandler(NotFoundAccountSequenceException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundAccountSequenceException(HttpServletRequest request, NotFoundAccountSequenceException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("NotFoundAccountSequenceException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("잘못된 계좌 종류"));
    }

    @ExceptionHandler(NotAccountOwnerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotAccountOwnerException(HttpServletRequest request, NotAccountOwnerException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("NotAccountOwnerException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("잘못된 계좌 번호"));
    }

    @ExceptionHandler(NotFoundAccountFullNumberException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundAccountFullNumberException(HttpServletRequest request, NotFoundAccountFullNumberException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("handleNotFoundAccountFullNumberException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("잘못된 계좌 번호"));
    }
}
