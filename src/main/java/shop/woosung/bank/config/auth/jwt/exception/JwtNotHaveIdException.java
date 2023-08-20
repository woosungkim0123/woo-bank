package shop.woosung.bank.config.auth.jwt.exception;

public class JwtNotHaveIdException extends RuntimeException {
    public JwtNotHaveIdException() {
        super("JWT 토큰에 ID가 없습니다.");
    }
}
