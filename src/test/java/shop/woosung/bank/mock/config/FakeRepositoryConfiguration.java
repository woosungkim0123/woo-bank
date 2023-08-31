package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.mock.repository.FakeAccountRepository;
import shop.woosung.bank.mock.repository.FakeAccountSequenceRepository;
import shop.woosung.bank.mock.repository.FakeAccountTypeNumberRepository;
import shop.woosung.bank.mock.repository.FakeUserRepository;
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

    @Bean
    public AccountSequenceRepository accountSequenceRepository() {
        return new FakeAccountSequenceRepository();
    }

    @Bean
    public AccountTypeNumberRepository accountTypeNumberRepository() {
        return new FakeAccountTypeNumberRepository();
    }
}
