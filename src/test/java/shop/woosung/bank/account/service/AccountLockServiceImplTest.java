package shop.woosung.bank.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.account.controller.port.AccountLockService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.account.handler.exception.NotEnoughBalanceException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotMatchAccountPasswordException;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.mock.util.FakePasswordEncoder;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountLockServiceImplTest {
    @InjectMocks
    private AccountLockServiceImpl accountLockService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountSequenceRepository accountSequenceRepository;
    @Spy
    private PasswordEncoder passwordEncoder = new FakePasswordEncoder("aaaa-bbbb-cccc");

    @DisplayName("계좌 입금에 성공한다.")
    @Test
    void deposit_account_success() {
        // given
        Long fullNumber = 123456789L;
        Long amount = 10000L;
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when
        Account result = accountLockService.depositAccountWithLock(fullNumber, amount);

        // then
        assertThat(result.getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getBalance()).isEqualTo(11000L);
    }

    @DisplayName("계좌 입금시 계좌 번호가 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void if_not_found_account_when_account_deposit_throw_exception() {
        // given
        Long fullNumber = 123456789L;
        Long amount = 10000L;

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountLockService.depositAccountWithLock(fullNumber, amount))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @DisplayName("계좌 출금에 성공한다.")
    @Test
    void withdraw_account_success() {
        // given
        User user = User.builder().id(1L).build();
        AccountWithdrawLockServiceDto accountWithdrawLockServiceDto = AccountWithdrawLockServiceDto.builder()
                .user(user)
                .fullNumber(123456789L)
                .password(1234L)
                .amount(1000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).password("aaaa-bbbb-cccc").user(user).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when
        Account result = accountLockService.withdrawWithLock(accountWithdrawLockServiceDto);

        // then
        assertThat(result.getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getBalance()).isEqualTo(0L);
        assertThat(result.getPassword()).isEqualTo("aaaa-bbbb-cccc");
        assertThat(result.getUser().getId()).isEqualTo(1L);
    }

    @DisplayName("계좌 출금시 계좌 번호가 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void if_not_found_account_when_account_withdraw_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountWithdrawLockServiceDto accountWithdrawLockServiceDto = AccountWithdrawLockServiceDto.builder()
                .user(user)
                .fullNumber(123456789L)
                .password(1234L)
                .amount(10000L)
                .build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountLockService.withdrawWithLock(accountWithdrawLockServiceDto))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @DisplayName("계좌 출금시 자신의 계좌가 아니면 예외를 발생시킨다.")
    @Test
    void if_not_my_account_when_account_withdraw_throw_exception() {
        // given
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        AccountWithdrawLockServiceDto accountWithdrawLockServiceDto = AccountWithdrawLockServiceDto.builder()
                .user(user1)
                .fullNumber(123456789L)
                .password(1234L)
                .amount(10000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).user(user2).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when & then
        assertThatThrownBy(() -> accountLockService.withdrawWithLock(accountWithdrawLockServiceDto))
                .isInstanceOf(NotAccountOwnerException.class);
    }

    @DisplayName("계좌 출금시 계좌 비밀번호가 일치하지 않으면 예외를 발생시킨다.")
    @Test
    void if_not_match_password_when_account_withdraw_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountWithdrawLockServiceDto accountWithdrawLockServiceDto = AccountWithdrawLockServiceDto.builder()
                .user(user)
                .fullNumber(123456789L)
                .password(1234L)
                .amount(10000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).password("dddd-eeee-ffff").user(user).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when & then
        assertThatThrownBy(() -> accountLockService.withdrawWithLock(accountWithdrawLockServiceDto))
                .isInstanceOf(NotMatchAccountPasswordException.class);
    }

    @DisplayName("계좌 출금시 잔액이 부족하면 예외를 발생시킨다.")
    @Test
    void if_not_enough_balance_when_account_withdraw_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountWithdrawLockServiceDto accountWithdrawLockServiceDto = AccountWithdrawLockServiceDto.builder()
                .user(user)
                .fullNumber(123456789L)
                .password(1234L)
                .amount(2000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).password("aaaa-bbbb-cccc").user(user).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when & then
        assertThatThrownBy(() -> accountLockService.withdrawWithLock(accountWithdrawLockServiceDto))
                .isInstanceOf(NotEnoughBalanceException.class);
    }
}