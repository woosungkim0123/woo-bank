package shop.woosung.bank.account.handler.exception;

public class NotMatchAccountPasswordException extends RuntimeException {
    public NotMatchAccountPasswordException() {
        super("계좌 비밀번호가 틀립니다.");
    }
}
