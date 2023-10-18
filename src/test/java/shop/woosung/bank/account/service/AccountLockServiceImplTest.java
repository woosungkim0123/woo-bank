package shop.woosung.bank.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.handler.exception.*;
import shop.woosung.bank.account.service.dto.AccountTransferLockResponseDto;
import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountLockServiceImplTest {
    @InjectMocks
    private AccountLockServiceImpl accountLockService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountSequenceRepository accountSequenceRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("새로운 계좌 번호를 가져오는 것에 성공한다.")
    @Test
    void get_account_new_number() {
        // given
        AccountSequence accountSequence = AccountSequence.builder().sequenceName(AccountType.NORMAL).nextValue(1000000L).incrementBy(1L).build();

        // stub
        when(accountSequenceRepository.findById(anyString())).thenReturn(Optional.of(accountSequence));

        // when
        Long result = accountLockService.getNewAccountNumber(AccountType.NORMAL);

        // then
        assertThat(result).isEqualTo(1000000L);
        assertThat(accountSequence.getNextValue()).isEqualTo(1000001L);
    }

    @DisplayName("새로운 계좌 번호를 가져올 때 존재하지 않는 타입이면 예외를 발생시킨다.")
    @Test
    void if_not_found_account_type_when_get_account_sequence_number_throw_exception() {
        // stub
        when(accountSequenceRepository.findById(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountLockService.getNewAccountNumber(AccountType.NORMAL))
                .isInstanceOf(NotFoundAccountSequenceException.class);
    }

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
                .password("1234")
                .amount(1000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).password("aaaa-bbbb-cccc").user(user).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

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
                .password("1234")
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
                .password("1234")
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
                .password("1234")
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
                .password("1234")
                .amount(2000L)
                .build();
        Account account = Account.builder().fullNumber(123456789L).balance(1000L).password("aaaa-bbbb-cccc").user(user).build();

        // stub
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(accountRepository.findByFullNumberWithPessimisticLock(anyLong())).thenReturn(Optional.of(account));

        // when & then
        assertThatThrownBy(() -> accountLockService.withdrawWithLock(accountWithdrawLockServiceDto))
                .isInstanceOf(NotEnoughBalanceException.class);
    }

    @DisplayName("계좌 이체에 성공한다.")
    @Test
    void transfer_account_success() {
        // given
        User user = User.builder().id(1L).build();
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).withdrawPassword(1234L).amount(10000L).depositFullNumber(123456789L).user(user).build();

        // stub
        Account withdrawAccount = Account.builder().id(1L).fullNumber(999999999L).balance(10000L).password("1234").user(user).build();
        Account depositAccount = Account.builder().id(2L).fullNumber(123456789L).balance(0L).build();
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(123456789L))).thenReturn(Optional.of(depositAccount));
        when(passwordEncoder.matches(eq("1234"), eq("1234"))).thenReturn(true);

        // when
        AccountTransferLockResponseDto result = accountLockService.transferWithLock(accountTransferLockServiceDto);

        // then
        assertThat(result.getWithdrawAccountDto().getId()).isEqualTo(1L);
        assertThat(result.getWithdrawAccountDto().getFullNumber()).isEqualTo(999999999L);
        assertThat(result.getWithdrawAccountDto().getBalance()).isEqualTo(0L);
        assertThat(result.getDepositAccountDto().getId()).isEqualTo(2L);
        assertThat(result.getDepositAccountDto().getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getDepositAccountDto().getBalance()).isEqualTo(10000L);
    }

    @DisplayName("같은 계좌에 이체를 시도하면 예외를 발생시킨다.")
    @Test
    void not_transfer_same_account_when_transfer_throw_exception() {
        // given
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).depositFullNumber(999999999L).build();

        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(SameAccountTransferException.class);
    }

    @DisplayName("계좌 이체시 출금 계좌가 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void if_not_exist_withdraw_account_when_account_transfer_throw_exception() {
        // given
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).build();

        // stub
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @DisplayName("계좌 이체시 입금 계좌가 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void if_not_exist_deposit_account_when_account_transfer_throw_exception() {
        // given
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).depositFullNumber(123456789L).build();

        // stub
        Account withdrawAccount = Account.builder().id(1L).fullNumber(999999999L).build();
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(123456789L))).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(NotFoundAccountFullNumberException.class);
    }

    @DisplayName("계좌 이체시 출금 계좌의 소유자가 아니라면 예외를 발생시킨다.")
    @Test
    void if_not_owner_withdraw_account_when_account_transfer_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).depositFullNumber(123456789L).user(user).build();

        // stub
        User anotherUser = User.builder().id(2L).build();
        Account withdrawAccount = Account.builder().id(1L).fullNumber(999999999L).user(anotherUser).build();
        Account depositAccount = Account.builder().id(2L).fullNumber(123456789L).build();
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(123456789L))).thenReturn(Optional.of(depositAccount));

        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(NotAccountOwnerException.class);
    }

    @DisplayName("계좌 이체시 출금 계좌의 비밀번호가 일치하지않으면 예외를 발생시킨다.")
    @Test
    void if_not_match_password_when_account_transfer_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).withdrawPassword(1235L).depositFullNumber(123456789L).user(user).build();

        // stub
        Account withdrawAccount = Account.builder().id(1L).fullNumber(999999999L).password("1234").user(user).build();
        Account depositAccount = Account.builder().id(2L).fullNumber(123456789L).build();
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(123456789L))).thenReturn(Optional.of(depositAccount));
        when(passwordEncoder.matches(eq("1235"), eq("1234"))).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(NotMatchAccountPasswordException.class);
    }

    @DisplayName("계좌 이체시 출금 계좌의 금액이 부족하면 예외를 발생시킨다.")
    @Test
    void if_not_enough_balance_when_account_transfer_throw_exception() {
        // given
        User user = User.builder().id(1L).build();
        AccountTransferLockServiceDto accountTransferLockServiceDto = AccountTransferLockServiceDto.builder().withdrawFullNumber(999999999L).withdrawPassword(1234L).amount(10000L).depositFullNumber(123456789L).user(user).build();

        // stub
        Account withdrawAccount = Account.builder().id(1L).fullNumber(999999999L).balance(9000L).password("1234").user(user).build();
        Account depositAccount = Account.builder().id(2L).fullNumber(123456789L).build();
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(999999999L))).thenReturn(Optional.of(withdrawAccount));
        when(accountRepository.findByFullNumberWithPessimisticLock(eq(123456789L))).thenReturn(Optional.of(depositAccount));
        when(passwordEncoder.matches(eq("1234"), eq("1234"))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> accountLockService.transferWithLock(accountTransferLockServiceDto))
                .isInstanceOf(NotEnoughBalanceException.class);
    }
}