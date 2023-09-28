package shop.woosung.bank.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountSequenceException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumberException;
import shop.woosung.bank.account.service.dto.*;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.repository.*;
import shop.woosung.bank.mock.util.FakePasswordEncoder;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountServiceImplTest {

    private AccountService accountService;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private FakeAccountSequenceRepository accountSequenceRepository;
    private FakeAccountTypeNumberRepository accountTypeNumberRepository;
    private FakePasswordEncoder passwordEncoder;

    private static final Long incrementByNumber = 1L;
    private static final Long initNormalTypeNumber = 232L;
    private static final Long initSavingTypeNumber = 787L;

    @BeforeEach
    void init() {
        Long initSequenceNumber = 11111111111L;

        userRepository = new FakeUserRepository();
        accountRepository = new FakeAccountRepository();
        accountSequenceRepository = new FakeAccountSequenceRepository();
        accountTypeNumberRepository = new FakeAccountTypeNumberRepository();
        passwordEncoder = new FakePasswordEncoder("aaaa_bbbb_cccc_dddd");

        this.accountService = AccountServiceImpl.builder()
                .passwordEncoder(passwordEncoder)
                .userRepository(userRepository)
                .accountRepository(accountRepository)
                .accountSequenceRepository(accountSequenceRepository)
                .accountTypeNumberRepository(accountTypeNumberRepository)
                .accountLockService(new AccountLockServiceImpl(accountRepository, passwordEncoder))
                .transactionRepository(new FakeTransactionRepository())
                .build();


        accountSequenceRepository.save(AccountSequence.builder().sequenceName(AccountType.NORMAL).nextValue(initSequenceNumber).incrementBy(incrementByNumber).build());
        accountSequenceRepository.save(AccountSequence.builder().sequenceName(AccountType.SAVING).nextValue(initSequenceNumber).incrementBy(incrementByNumber).build());
        accountTypeNumberRepository.save(AccountTypeNumber.builder().accountType(AccountType.NORMAL).number(initNormalTypeNumber).build());
        accountTypeNumberRepository.save(AccountTypeNumber.builder().accountType(AccountType.SAVING).number(initSavingTypeNumber).build());
    }

    @DisplayName("자신의 모든 계좌 목록을 가져온다")
    @Test
    public void get_my_all_account() {
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

    @DisplayName("계좌를 등록하면 계좌번호를 부여하고 생성한다")
    @Test
    public void account_register_success_test1() {
        // given
        Long fullNumber = Long.parseLong(initNormalTypeNumber + "" + 11111111111L);
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder().type(AccountType.NORMAL).password("1234").build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestServiceDto, user);

        // then
        assertThat(accountRegisterResponseDto.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto.getFullNumber()).isEqualTo(fullNumber);
        assertThat(accountRegisterResponseDto.getBalance()).isEqualTo(0L);
    }

    @DisplayName("계좌 종류에 따라 다른 타입 번호가 부여되고 중복된 계좌번호는 부여되지 않는다.")
    @Test
    public void account_register_success_test2() {
        // given
        User user1 = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        User user2 = userRepository.save(User.builder().email("test2@tset.com").name("test2").build());
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto1 = AccountRegisterRequestServiceDto.builder().type(AccountType.NORMAL).password("1234").build();
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto2 = AccountRegisterRequestServiceDto.builder().type(AccountType.SAVING).password("1234").balance(1000L).build();
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto3 = AccountRegisterRequestServiceDto.builder().type(AccountType.NORMAL).password("1234").balance(2000L).build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto1 = accountService.register(accountRegisterRequestServiceDto1, user1);
        AccountRegisterResponseDto accountRegisterResponseDto2 = accountService.register(accountRegisterRequestServiceDto2, user1);
        AccountRegisterResponseDto accountRegisterResponseDto3 = accountService.register(accountRegisterRequestServiceDto3, user2);

        // then
        assertThat(accountRegisterResponseDto1.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto1.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto1.getFullNumber()).isEqualTo(Long.parseLong(initNormalTypeNumber + "" + 11111111111L));
        assertThat(accountRegisterResponseDto1.getBalance()).isEqualTo(0L);
        assertThat(accountRegisterResponseDto2.getId()).isEqualTo(2L);
        assertThat(accountRegisterResponseDto2.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto2.getFullNumber()).isEqualTo(Long.parseLong(initSavingTypeNumber + "" + 11111111111L));
        assertThat(accountRegisterResponseDto2.getBalance()).isEqualTo(1000L);
        assertThat(accountRegisterResponseDto3.getId()).isEqualTo(3L);
        assertThat(accountRegisterResponseDto3.getNumber()).isEqualTo(11111111112L);
        assertThat(accountRegisterResponseDto3.getFullNumber()).isEqualTo(Long.parseLong(initNormalTypeNumber + "" + 11111111112L));
        assertThat(accountRegisterResponseDto3.getBalance()).isEqualTo(2000L);
    }

    @DisplayName("계좌 종류에 해당 하는 타입 넘버가 없으면 예외를 뱉는다.")
    @Test
    public void account_register_fail_test1() {
        // given & when
        accountTypeNumberRepository.deleteAll();
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder().type(AccountType.NORMAL).password("1234").build();

        // then
        assertThatThrownBy(() -> accountService.register(accountRegisterRequestServiceDto, user))
                .isInstanceOf(NotFoundAccountTypeNumberException.class);
    }

    @DisplayName("계좌 종류에 해당하는 시퀀스 값이 없으면 예외를 뱉는다.")
    @Test
    public void account_register_fail_test2() {
        // given & when
        accountSequenceRepository.deleteAll();
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder().type(AccountType.NORMAL).password("1234").build();

        // then
        assertThatThrownBy(() -> accountService.register(accountRegisterRequestServiceDto, user))
                .isInstanceOf(NotFoundAccountSequenceException.class);
    }

    @DisplayName("자신의 계좌를 없앨 수 있다.")
    @Test
    public void account_delete_success_test() {
        // given
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        accountRepository.save(Account.builder().number(11111111L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build());

        // when
        accountService.deleteAccount(23211111111L, user);

        // then
        Optional<Account> account = accountRepository.findByFullNumber(23211111111L);
        assertThat(account.isPresent()).isFalse();
    }

    @DisplayName("계좌번호가 일치하지 않으면 삭제할 수 없다.")
    @Test
    public void account_delete_fail_test1() {
        // given & when
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        accountRepository.save(Account.builder().number(11111111L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build());
        Long notExistAccount = 23299999999L;

        // then
        assertThatThrownBy(() -> accountService.deleteAccount(notExistAccount, user))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @Test
    void account_deposit_success_test() {
        // given
        accountRepository.save(Account.builder().number(11111111L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).build());
        AccountDepositRequestServiceDto accountDepositRequestServiceDto = AccountDepositRequestServiceDto.builder().fullNumber(23211111111L).amount(1000L).transactionType(TransactionType.DEPOSIT).sender("ATM").tel("010-1234-1234").build();

        // when
        AccountDepositResponseDto result = accountService.deposit(accountDepositRequestServiceDto);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullNumber()).isEqualTo(23211111111L);
        assertThat(result.getTransaction().getId()).isEqualTo(1L);
        assertThat(result.getTransaction().getType()).isEqualTo(TransactionType.DEPOSIT.name());
        assertThat(result.getTransaction().getSender()).isEqualTo("ATM");
        assertThat(result.getTransaction().getAmount()).isEqualTo(1000L);
        assertThat(result.getTransaction().getDepositAccountBalance()).isEqualTo(2000L);
        assertThat(result.getTransaction().getTel()).isEqualTo("010-1234-1234");
        assertThat(result.getTransaction().getCreatedAt()).isEqualTo("2023-08-11T15:30");
    }
    
    // 출금 테스트 만들어야함
}