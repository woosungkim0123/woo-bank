package shop.woosung.bank.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.FakeAccountRepository;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;


import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceImplTest {

    private AccountService accountService;
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {

        userRepository = new FakeUserRepository();
        accountRepository = new FakeAccountRepository();
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
        AccountListResponseDto result = accountService.getAccountList(user1);

        // then
        assertThat(result.getAccounts().size()).isEqualTo(2);
        assertThat(result.getAccounts().get(0).getNumber()).isEqualTo(1111111111L);
        assertThat(result.getAccounts().get(0).getBalance()).isEqualTo(1000L);
        assertThat(result.getAccounts().get(1).getNumber()).isEqualTo(1111111112L);
        assertThat(result.getAccounts().get(1).getBalance()).isEqualTo(2000L);
    }

    @Test
    public void 계좌등록시_서비스_첫_고객이면_11111111111로_배정된다() throws Exception {
        // given
        User user1 = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder().password(1234L).build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, user1);

        // then
        assertThat(accountRegisterResponseDto.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto.getBalance()).isEqualTo(0L);
    }

    @Test
    public void 계좌등록시_가장_높은_계좌번호에서_1을_더한값을_계좌번호로_생성한다() throws Exception {
        // given
        User user1 = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        accountRepository.save(Account.builder().number(11111111111L).balance(1000L).user(user1).build());
        accountRepository.save(Account.builder().number(11111111115L).balance(1000L).user(user1).build());
        accountRepository.save(Account.builder().number(99999999998L).balance(1000L).user(user1).build());
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder().password(1234L).build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, user1);

        // then
        assertThat(accountRegisterResponseDto.getId()).isEqualTo(4L);
        assertThat(accountRegisterResponseDto.getNumber()).isEqualTo(99999999999L);
        assertThat(accountRegisterResponseDto.getBalance()).isEqualTo(0L);
    }
}