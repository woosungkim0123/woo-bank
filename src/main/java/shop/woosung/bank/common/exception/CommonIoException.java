package shop.woosung.bank.common.exception;

public class CommonIoException extends RuntimeException {
    public CommonIoException() {
        super("IO 오류");
    }
}
