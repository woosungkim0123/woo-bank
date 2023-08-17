package shop.woosung.bank.config.auth.jwt.exception;

public class JwtIdConversionException extends RuntimeException {
    public JwtIdConversionException() {
        super("JWT 토큰의 ID를 Long 타입으로 변환할 수 없습니다.");
    }
}
