package shop.woosung.bank.common.exception;

public class ObjectConvertJsonException extends RuntimeException {
    public ObjectConvertJsonException() {
        super("JSON 변환 오류");
    }
}
