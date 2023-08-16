package shop.woosung.bank.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;

import shop.woosung.bank.user.domain.UserRole;

import java.util.Date;

@Component
public class JwtTokenManager implements JwtTokenProvider {
    @Override
    public String createToken(LoginUser loginUser) {
        return JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));
    }

    @Override
    public User verifyToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        return User.builder().id(id).role(UserRole.valueOf(role)).build();
    }
}
