package shop.woosung.bank.account.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountTransferRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawRequestServiceDto;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceToServiceConverterTest {

    @DisplayName("계좌 출금시 계좌 서비스의 데이터를 계좌 락 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_withdraw_lock_service_dto_convert() {
        // given
        AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto = AccountWithdrawRequestServiceDto.builder()
                .fullNumber(123456789L)
                .password("1234")
                .amount(10000L)
                .transactionType(TransactionType.WITHDRAW)
                .build();
        User user = User.builder().id(1L).name("test").build();

        // when
        AccountWithdrawLockServiceDto result = AccountServiceToServiceConverter.accountWithdrawLockServiceDtoConvert(accountWithdrawRequestServiceDto, user);

        // then
        assertThat(result.getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getAmount()).isEqualTo(10000L);
        assertThat(result.getUser()).isEqualTo(user);
    }

    @DisplayName("계좌 이체시 계좌 서비스의 데이터를 계좌 락 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_transfer_lock_service_dto_convert() {
        // given
        AccountTransferRequestServiceDto accountTransferRequestServiceDto = AccountTransferRequestServiceDto.builder()
                .withdrawFullNumber(123456789L)
                .withdrawPassword(1234L)
                .depositFullNumber(987654321L)
                .amount(10000L)
                .transactionType(TransactionType.TRANSFER)
                .build();
        User user = User.builder().id(1L).name("test").build();

        // when
        AccountTransferLockServiceDto result = AccountServiceToServiceConverter.accountTransferLockServiceDtoConvert(accountTransferRequestServiceDto, user);

        // then
        assertThat(result.getWithdrawFullNumber()).isEqualTo(123456789L);
        assertThat(result.getWithdrawPassword()).isEqualTo(1234L);
        assertThat(result.getDepositFullNumber()).isEqualTo(987654321L);
        assertThat(result.getAmount()).isEqualTo(10000L);
        assertThat(result.getUser()).isEqualTo(user);
    }
}