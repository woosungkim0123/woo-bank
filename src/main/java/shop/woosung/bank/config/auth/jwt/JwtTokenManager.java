package shop.woosung.bank.config.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.jwt.exception.JwtIdConversionException;
import shop.woosung.bank.config.auth.jwt.exception.JwtNotHaveIdException;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenManager implements JwtTokenProvider {

    @Override
    public String create(LoginUser loginUser) {
        return JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));
    }

    @Override
    public Long verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Claim JwtIdClaim = getIdByJwt(decodedJWT);

        return convertIdClaimToLong(JwtIdClaim);
    }

    private long convertIdClaimToLong(Claim idClaim) throws JwtIdConversionException {
        try {
            return idClaim.asLong();
        } catch (NumberFormatException exception) {
            log.error("idClaim = {}", idClaim);
            log.error("NumberFormatException  = {}", exception.getMessage());
            throw new JwtIdConversionException();
        }
    }

    private Claim getIdByJwt(DecodedJWT decodedJWT) throws JwtNotHaveIdException {
        Claim idClaim = decodedJWT.getClaim("id");
        if (idClaim.isNull()) {
            throw new JwtNotHaveIdException();
        }
        return idClaim;
    }
}
