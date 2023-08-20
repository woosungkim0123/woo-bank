package shop.woosung.bank.config.auth.jwt;

import shop.woosung.bank.config.auth.LoginUser;

public class JwtProcess {

    public static String create(JwtTokenProvider jwtTokenProvider, LoginUser loginUser) {
        String jwtToken = jwtTokenProvider.create(loginUser);
        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    public static Long verify(JwtTokenProvider jwtTokenProvider, String token) {
        return jwtTokenProvider.verify(token);
    }
}
