package shop.woosung.bank.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.FakeAccountRepository;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceImplTest {

    private AccountService accountService;
    private final UserRepository userRepository = new FakeUserRepository();
    private final AccountRepository accountRepository = new FakeAccountRepository();

    @BeforeEach
    void init() {
        this.accountService = AccountServiceImpl.builder()
                .userRepository(userRepository)
                .accountRepository(accountRepository)
                .build();
    }
    
    @Test
    public void userId를_사용하여_해당_유저가_가진_모든_계좌_리스트를_찾을_수_있다() {
        // given
        User user1 = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        User user2 = userRepository.save(User.builder().email("test2@tset.com").name("test2").build());
        accountRepository.save(Account.builder().number(1111111111L).balance(1000L).user(user1).build());
        accountRepository.save(Account.builder().number(1111111112L).balance(2000L).user(user1).build());
        accountRepository.save(Account.builder().number(1111111113L).balance(3000L).user(user2).build());

        // when
        AccountListResponseDto result = accountService.getAccountList(user1.getId());

        // then
        assertThat(result.getAccounts().size()).isEqualTo(2);
        assertThat(result.getAccounts().get(0).getNumber()).isEqualTo(1111111111L);
        assertThat(result.getAccounts().get(0).getBalance()).isEqualTo(1000L);
        assertThat(result.getAccounts().get(1).getNumber()).isEqualTo(1111111112L);
        assertThat(result.getAccounts().get(1).getBalance()).isEqualTo(2000L);
    }

}