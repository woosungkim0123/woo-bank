package shop.woosung.bank.account.handler.exception;

public class NotFoundAccountSequence extends RuntimeException {
    public NotFoundAccountSequence() {
        super("계좌 시퀀스 정보를 찾을 수 없습니다.");
    }
}
