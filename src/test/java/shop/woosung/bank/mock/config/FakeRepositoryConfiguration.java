package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.FakeAccountRepository;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.user.service.port.UserRepository;

public class FakeRepositoryConfiguration {

    @Bean
    public UserRepository userRepository() {
        return new FakeUserRepository();
    }

    @Bean
    public AccountRepository accountRepository() {
        return new FakeAccountRepository();
    }
}
