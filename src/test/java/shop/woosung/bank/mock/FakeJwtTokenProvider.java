package shop.woosung.bank.mock;

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

    public void init() {
        this.token = null;
        this.userId = null;
        this.currentAt = null;
        this.expiredAt = null;
        this.error = null;
    }

    @Override
    public String create(LoginUser loginUser) {
        return token;
    }

    @Override
    public Long verify(String receiveToken) {
        System.out.println("token" + token);
        checkError();
        checkSameToken(receiveToken);
        checkExpireToken();
        return userId;
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
