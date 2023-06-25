package shop.woosung.bank.config.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import com.auth0.jwt.JWT;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;

import java.util.Date;

@Slf4j
public class JwtProcess {

    public static String create(LoginUser loginUser) {

        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwtToken;
    }

    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        return new LoginUser(user);
    }
}
