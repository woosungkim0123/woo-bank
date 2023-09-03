package shop.woosung.bank.account.handler.exception;

public class NotFoundAccountFullNumber extends RuntimeException {
    public NotFoundAccountFullNumber(Long accountFullNumber) {
        super(accountFullNumber + " 계좌 번호 정보를 찾을 수 없습니다.");
    }
}
