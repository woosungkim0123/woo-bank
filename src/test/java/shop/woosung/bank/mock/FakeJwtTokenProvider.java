package shop.woosung.bank.mock;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.config.auth.jwt.exception.JwtExpiredException;
import shop.woosung.bank.config.auth.jwt.exception.JwtIdConversionException;
import shop.woosung.bank.config.auth.jwt.exception.JwtNotHaveIdException;
import shop.woosung.bank.config.auth.jwt.exception.JwtVerifyException;

import java.time.LocalDateTime;
import java.util.Objects;

public class FakeJwtTokenProvider implements JwtTokenProvider {
    public String token;
    public Long userId;
    public LocalDateTime currentAt;
    public LocalDateTime expiredAt;
    public String error;


    @Override
    public String create(LoginUser loginUser) {
        return token;
    }

    @Override
    public Long verify(String receiveToken) {
        System.out.println("하이");
        System.out.println("this.currentAt = " + this.currentAt);
        System.out.println("this.token = " + this.token);
        checkSameToken(receiveToken);
        checkExpireToken();
        checkError();

        return 1L;
    }

    private void checkSameToken(String receiveToken) {
        if (!Objects.equals(this.token, receiveToken)) {
            throw new JwtVerifyException();
        }
    }

    private void checkExpireToken() {
        if (currentAt != null && expiredAt != null && currentAt.isAfter(expiredAt)) {
            throw new JwtExpiredException();
        }
    }

    private void checkError() {
        if (error != null) {
            if (Objects.equals(error, "id")) {
                throw new JwtNotHaveIdException();
            } else if (Objects.equals(error, "idType")) {
                throw new JwtIdConversionException();
            }
        }
    }
}
