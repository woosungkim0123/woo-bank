package shop.woosung.bank.config.auth.jwt.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
        super("토큰이 만료 되었습니다.");
    }
}
