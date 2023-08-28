package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.mock.FakeJwtTokenProvider;

public class FakeJwtConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new FakeJwtTokenProvider();
    }
}
