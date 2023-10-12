package shop.woosung.bank.config.auth.exception;

public class JwtVerifyException extends RuntimeException {
    public JwtVerifyException() {
        super("토큰 검증에 실패 했습니다.");
    }
}
