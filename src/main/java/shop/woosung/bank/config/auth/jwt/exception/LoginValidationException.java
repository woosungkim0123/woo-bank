package shop.woosung.bank.config.auth.jwt.exception;

public class LoginValidationException extends RuntimeException {
    public LoginValidationException(String message) {
        super(message);
    }
}
