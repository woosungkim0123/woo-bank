package shop.woosung.bank.config.jwt;

import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;

public interface JwtTokenProvider {

    String createToken(LoginUser loginUser);

    User verifyToken(String token);
}
