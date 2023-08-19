package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.user.service.port.UserRepository;

@Configuration
public class FakeRepositoryConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new FakeUserRepository();
    }
}
