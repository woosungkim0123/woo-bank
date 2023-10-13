package shop.woosung.bank.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.account.controller.port.AccountLockService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumberException;
import shop.woosung.bank.account.service.dto.*;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountLockService accountLockService;
    @Mock
    private AccountTypeNumberRepository accountTypeNumberRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("자신의 모든 계좌 목록을 가져온다.")
    @Test
    void get_my_all_accounts() {
        // given
        User user = User.builder().id(1L).name("test").build();

        // stub
        when(accountRepository.findByUserId(anyLong())).thenReturn(
                List.of(
                        Account.builder().id(1L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build(),
                        Account.builder().id(2L).fullNumber(23211111112L).type(AccountType.SAVING).balance(2000L).user(user).build()
                ));

        // when
        AccountListResponseDto result = accountService.getAccountList(user);

        // then
        assertThat(result.getUsername()).isEqualTo("test");
        assertThat(result.getAccounts().size()).isEqualTo(2);
        assertThat(result.getAccounts().get(0).getId()).isEqualTo(1L);
        assertThat(result.getAccounts().get(0).getFullNumber()).isEqualTo(23211111111L);
        assertThat(result.getAccounts().get(0).getType()).isEqualTo(AccountType.NORMAL);
        assertThat(result.getAccounts().get(0).getBalance()).isEqualTo(1000L);
        assertThat(result.getAccounts().get(1).getId()).isEqualTo(2L);
        assertThat(result.getAccounts().get(1).getFullNumber()).isEqualTo(23211111112L);
        assertThat(result.getAccounts().get(1).getType()).isEqualTo(AccountType.SAVING);
        assertThat(result.getAccounts().get(1).getBalance()).isEqualTo(2000L);
    }

    @DisplayName("새로운 계좌를 등록한다.")
    @Test
    void register_new_account() {
        // given
        User user = User.builder().id(1L).name("test").build();
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder().password("1234").balance(1000L).type(AccountType.NORMAL).build();

        // stub
        when(accountTypeNumberRepository.findById(any())).thenReturn(Optional.of(AccountTypeNumber.builder().accountType(AccountType.NORMAL).number(232L).build()));
        when(accountLockService.getNewAccountNumber(any())).thenReturn(11111111111L);
        when(accountRepository.save(any())).thenReturn(Account.builder().id(1L).fullNumber(23211111111L).number(11111111111L).type(AccountType.NORMAL).balance(1000L).build());

        // when
        AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestServiceDto, user);

        // then
        assertThat(accountRegisterResponseDto.getId()).isEqualTo(1L);
        assertThat(accountRegisterResponseDto.getNumber()).isEqualTo(11111111111L);
        assertThat(accountRegisterResponseDto.getFullNumber()).isEqualTo(23211111111L);
        assertThat(accountRegisterResponseDto.getBalance()).isEqualTo(1000L);
    }

    @DisplayName("계좌 가입시 계좌 타입에 해당하는 타입 번호를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void if_not_found_account_type_when_register_account_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder().password("1234").balance(1000L).type(AccountType.NORMAL).build();

        // stub
        when(accountTypeNumberRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountService.register(accountRegisterRequestServiceDto, user))
                .isInstanceOf(NotFoundAccountTypeNumberException.class);
    }

    @DisplayName("자신의 계좌를 삭제할 수 있다.")
    @Test
    void delete_my_account() {
        // given
        Long accountFullNumber = 23211111111L;
        User user = User.builder().id(1L).email("test1@tset.com").name("test1").build();

        // stub
        when(accountRepository.findByFullNumber(anyLong())).thenReturn(Optional.of(Account.builder().id(1L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build()));

        // when
        accountService.deleteAccount(accountFullNumber, user);

        // then
        verify(accountRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("계좌 삭제시 계좌번호를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    public void if_not_found_account_number_when_delete_account_throw_exception() {
        // given
        User user = User.builder().email("test1@tset.com").name("test1").build();
        Long notExistAccount = 23299999999L;

        // stub
        when(accountRepository.findByFullNumber(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountService.deleteAccount(notExistAccount, user))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @DisplayName("계좌 삭제시 자신의 계좌가 아니라면 예외를 발생시킨다.")
    @Test
    public void if_not_account_owner_when_delete_account_throw_exception() {
        // given
        Long accountNumber = 23211111111L;
        User user = User.builder().id(1L).email("test1@tset.com").name("test1").build();
        User anotherUser = User.builder().id(2L).email("test2test.com").name("test2").build();
        Account account = Account.builder().id(1L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).user(user).build();

        // stub
        when(accountRepository.findByFullNumber(anyLong())).thenReturn(Optional.of(account));

        // when & then
        assertThatThrownBy(() -> accountService.deleteAccount(accountNumber, anotherUser))
                .isInstanceOf(NotAccountOwnerException.class);
    }

    @DisplayName("계좌에 돈을 입금한다.")
    @Test
    void deposit_account() {
        // given
        AccountDepositRequestServiceDto accountDepositRequestServiceDto = AccountDepositRequestServiceDto.builder().fullNumber(23211111111L).amount(1000L).transactionType(TransactionType.DEPOSIT).sender("ATM").tel("010-1234-1234").build();
        Account account = Account.builder().id(1L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).build();
        Transaction transaction = Transaction.builder().id(1L).type(TransactionType.DEPOSIT).sender("ATM").amount(1000L).depositAccountBalance(2000L).tel("010-1234-1234").createdAt(LocalDateTime.of(2023, 8, 11, 15, 30, 00)).build();

        // stub
        when(accountLockService.depositAccountWithLock(anyLong(), anyLong())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(transaction);

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

    @DisplayName("계좌에서 돈을 출금한다.")
    @Test
    void withdraw_account() {
        // given
        AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto = AccountWithdrawRequestServiceDto.builder().fullNumber(23211111111L).amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        Account account = Account.builder().id(1L).fullNumber(23211111111L).type(AccountType.NORMAL).balance(1000L).build();
        User user = User.builder().id(1L).build();
        Transaction transaction = Transaction.builder().id(1L).type(TransactionType.WITHDRAW).sender("ATM").receiver("23211111111").amount(1000L).withdrawAccountBalance(2000L).createdAt(LocalDateTime.of(2023, 8, 11, 15, 30, 00)).build();

        // stub
        when(accountLockService.withdrawWithLock(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountWithdrawResponseDto result = accountService.withdraw(accountWithdrawRequestServiceDto, user);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullNumber()).isEqualTo(23211111111L);
        assertThat(result.getBalance()).isEqualTo(1000L);
        assertThat(result.getTransaction().getId()).isEqualTo(1L);
        assertThat(result.getTransaction().getType()).isEqualTo(TransactionType.WITHDRAW.name());
        assertThat(result.getTransaction().getSender()).isEqualTo("ATM");
        assertThat(result.getTransaction().getReceiver()).isEqualTo("23211111111");
        assertThat(result.getTransaction().getAmount()).isEqualTo(1000L);
        assertThat(result.getTransaction().getWithdrawAccountBalance()).isEqualTo(2000L);
        assertThat(result.getTransaction().getCreatedAt()).isEqualTo("2023-08-11T15:30");
    }
}