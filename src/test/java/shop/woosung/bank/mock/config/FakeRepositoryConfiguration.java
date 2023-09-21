package shop.woosung.bank.mock.config;

import org.springframework.context.annotation.Bean;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.mock.repository.*;
import shop.woosung.bank.transaction.service.port.TransactionRepository;

public class FakeRepositoryConfiguration {

    @Bean
    public FakeUserRepository userRepository() {
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

    @Bean
    public TransactionRepository transactionRepository() {
        return new FakeTransactionRepository();
    }
}
