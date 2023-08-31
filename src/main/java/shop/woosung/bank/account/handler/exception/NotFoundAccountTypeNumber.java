package shop.woosung.bank.account.handler.exception;

public class NotFoundAccountTypeNumber extends RuntimeException {
    public NotFoundAccountTypeNumber() {
        super("계좌 타입에 해당되는 타입 번호가 없습니다.");
    }
}
