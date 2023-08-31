package shop.woosung.bank.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.handler.exception.NotFoundAccountSequence;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumber;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.repository.FakeAccountRepository;
import shop.woosung.bank.mock.repository.FakeAccountSequenceRepository;
import shop.woosung.bank.mock.repository.FakeAccountTypeNumberRepository;
import shop.woosung.bank.mock.repository.FakeUserRepository;
import shop.woosung.bank.mock.util.FakePasswordEncoder;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountServiceImplTest {

    private AccountService accountService;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private FakeAccountSequenceRepository accountSequenceRepository;
    private FakeAccountTypeNumberRepository accountTypeNumberRepository;

    private final Long initSequenceNumber = 11111111111L;
    private static final Long incrementByNumber = 1L;
    private static final Long initNormalTypeNumber = 232L;
    private static final Long initSavingTypeNumber = 787L;

    @BeforeEach
    void init() {
        userRepository = new FakeUserRepository();
        accountRepository = new FakeAccountRepository();
        accountSequenceRepository = new FakeAccountSequenceRepository();
        accountTypeNumberRepository = new FakeAccountTypeNumberRepository();

        this.accountService = AccountServiceImpl.builder()
                .passwordEncoder(new FakePasswordEncoder("aaaa_bbbb_cccc_dddd"))
                .userRepository(userRepository)
                .accountRepository(accountRepository)
                .accountSequenceRepository(accountSequenceRepository)
                .accountTypeNumberRepository(accountTypeNumberRepository)
                .build();

        accountSequenceRepository.save(AccountSequence.builder().sequenceName(AccountType.NORMAL).nextValue(initSequenceNumber).incrementBy(incrementByNumber).build());
        accountSequenceRepository.save(AccountSequence.builder().sequenceName(AccountType.SAVING).nextValue(initSequenceNumber).incrementBy(incrementByNumber).build());
        accountTypeNumberRepository.save(AccountTypeNumber.builder().accountType(AccountType.NORMAL).number(initNormalTypeNumber).build());
        accountTypeNumberRepository.save(AccountTypeNumber.builder().accountType(AccountType.SAVING).number(initSavingTypeNumber).build());
    }

    @DisplayName("자신이 가진 모든 계좌 리스트를 조회할 수 있다.")
    @Test
    public void get_account_list_success_test() {
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
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder().type(AccountType.NORMAL).password("1234").build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, user);

        // then
        assertThat(accountRegisterResponseDto.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto.getFullnumber()).isEqualTo(fullNumber);
        assertThat(accountRegisterResponseDto.getBalance()).isEqualTo(0L);
    }

    @DisplayName("계좌 종류에 따라 다른 타입 번호가 부여되고 중복된 계좌번호는 부여되지 않는다.")
    @Test
    public void account_register_success_test2() {
        // given
        User user1 = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        User user2 = userRepository.save(User.builder().email("test2@tset.com").name("test2").build());
        AccountRegisterRequestDto accountRegisterRequestDto1 = AccountRegisterRequestDto.builder().type(AccountType.NORMAL).password("1234").build();
        AccountRegisterRequestDto accountRegisterRequestDto2 = AccountRegisterRequestDto.builder().type(AccountType.SAVING).password("1234").balance(1000L).build();
        AccountRegisterRequestDto accountRegisterRequestDto3 = AccountRegisterRequestDto.builder().type(AccountType.NORMAL).password("1234").balance(2000L).build();

        // when
        AccountRegisterResponseDto accountRegisterResponseDto1 = accountService.register(accountRegisterRequestDto1, user1);
        AccountRegisterResponseDto accountRegisterResponseDto2 = accountService.register(accountRegisterRequestDto2, user1);
        AccountRegisterResponseDto accountRegisterResponseDto3 = accountService.register(accountRegisterRequestDto3, user2);

        // then
        assertThat(accountRegisterResponseDto1.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto1.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto1.getFullnumber()).isEqualTo(Long.parseLong(initNormalTypeNumber + "" + 11111111111L));
        assertThat(accountRegisterResponseDto1.getBalance()).isEqualTo(0L);
        assertThat(accountRegisterResponseDto2.getId()).isEqualTo(2L);
        assertThat(accountRegisterResponseDto2.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto2.getFullnumber()).isEqualTo(Long.parseLong(initSavingTypeNumber + "" + 11111111111L));
        assertThat(accountRegisterResponseDto2.getBalance()).isEqualTo(1000L);
        assertThat(accountRegisterResponseDto3.getId()).isEqualTo(3L);
        assertThat(accountRegisterResponseDto3.getNumber()).isEqualTo(11111111112L);
        assertThat(accountRegisterResponseDto3.getFullnumber()).isEqualTo(Long.parseLong(initNormalTypeNumber + "" + 11111111112L));
        assertThat(accountRegisterResponseDto3.getBalance()).isEqualTo(2000L);
    }

    @DisplayName("계좌 종류에 해당 하는 타입 넘버가 없으면 예외를 뱉는다.")
    @Test
    public void account_register_fail_test1() {
        // given & when
        accountTypeNumberRepository.deleteAll();
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder().type(AccountType.NORMAL).password("1234").build();

        // then
        assertThatThrownBy(() -> accountService.register(accountRegisterRequestDto, user))
                .isInstanceOf(NotFoundAccountTypeNumber.class);
    }

    @DisplayName("계좌 종류에 해당하는 시퀀스 값이 없으면 예외를 뱉는다.")
    @Test
    public void account_register_fail_test2() {
        // given & when
        accountSequenceRepository.deleteAll();
        User user = userRepository.save(User.builder().email("test1@tset.com").name("test1").build());
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder().type(AccountType.NORMAL).password("1234").build();

        // then
        assertThatThrownBy(() -> accountService.register(accountRegisterRequestDto, user))
                .isInstanceOf(NotFoundAccountSequence.class);
    }
}