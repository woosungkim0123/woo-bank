package shop.woosung.bank.config.auth.jwt;

import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;

public interface JwtTokenProvider {

    String create(LoginUser loginUser);

    Long verify(String token);
}
