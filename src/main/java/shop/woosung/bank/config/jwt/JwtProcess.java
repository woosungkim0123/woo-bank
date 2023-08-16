package shop.woosung.bank.config.jwt;

import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;

public class JwtProcess {

    public static String create(JwtTokenProvider jwtTokenProvider, LoginUser loginUser) {
        String jwtToken = jwtTokenProvider.createToken(loginUser);
        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    public static LoginUser verify(JwtTokenProvider jwtTokenProvider, String token) {
        User user = jwtTokenProvider.verifyToken(token);
        return new LoginUser(user);
    }
}
