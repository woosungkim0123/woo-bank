package shop.woosung.bank.account.handler.exception;

public class NotAccountOwnerException extends RuntimeException {
    public NotAccountOwnerException() {
        super("계좌 소유자가 아닙니다.");
    }
}
