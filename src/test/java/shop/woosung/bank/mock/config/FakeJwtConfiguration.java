package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.mock.FakeJwtTokenProvider;

@Configuration
public class FakeJwtConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new FakeJwtTokenProvider();
    }
}
