package shop.woosung.bank.common.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String requestEmail, String findEmail, String message) {
        super(message);
    }
}
