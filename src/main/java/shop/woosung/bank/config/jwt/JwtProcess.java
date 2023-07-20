package shop.woosung.bank.config.jwt;

import lombok.extern.slf4j.Slf4j;

import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.domain.user.User;

@Slf4j
public class JwtProcess {

    public static String create(JwtHolder jwtHolder, LoginUser loginUser) {
        String jwtToken = jwtHolder.createToken(loginUser);
        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    public static LoginUser verify(JwtHolder jwtHolder, String token) {
        User user = jwtHolder.verifyToken(token);
        return new LoginUser(user);
    }
}
