package shop.woosung.bank.account.handler.exception;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException(Long accountNumber) {
        super("같은 계좌에 입금할 수 없습니다. accountNumber : " + accountNumber);
    }
}
