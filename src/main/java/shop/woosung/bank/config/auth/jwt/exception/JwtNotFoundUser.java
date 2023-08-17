package shop.woosung.bank.config.auth.jwt.exception;

public class JwtNotFoundUser extends RuntimeException {
public JwtNotFoundUser() {
        super("JWT 토큰에 해당하는 유저가 없습니다.");
    }
}
