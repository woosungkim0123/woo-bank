package shop.woosung.bank.account.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.dto.AccountDepositRequestDto;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.dto.AccountTransferRequestDto;
import shop.woosung.bank.account.controller.dto.AccountWithdrawRequestDto;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.dto.AccountDepositRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountTransferRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawRequestServiceDto;
import shop.woosung.bank.transaction.domain.TransactionType;

import static org.assertj.core.api.Assertions.assertThat;

class AccountControllerToServiceConverterTest {

    @DisplayName("계좌 등록을 위해 컨트롤러로 전달된 객체를 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_register_request_dto_convert() {
        // given
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password("1234")
                .balance(10000)
                .type(AccountType.NORMAL)
                .build();

        // when
        AccountRegisterRequestServiceDto result = AccountControllerToServiceConverter.accountRegisterRequestConvert(accountRegisterRequestDto);

        // then
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getBalance()).isEqualTo(10000);
        assertThat(result.getType()).isEqualTo(AccountType.NORMAL);
    }

    @DisplayName("계좌 입금을 위해 컨트롤러로 전달된 객체를 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_deposit_request_dto_convert() {
        // given
        AccountDepositRequestDto accountDepositRequestDto = AccountDepositRequestDto.builder()
                .fullNumber(123456789L)
                .amount(10000L)
                .transactionType(TransactionType.DEPOSIT)
                .sender("test")
                .tel("01012345678")
                .build();

        // when
        AccountDepositRequestServiceDto result = AccountControllerToServiceConverter.accountDepositRequestConvert(accountDepositRequestDto);

        // then
        assertThat(result.getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getAmount()).isEqualTo(10000L);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(result.getSender()).isEqualTo("test");
        assertThat(result.getTel()).isEqualTo("01012345678");
    }

    @DisplayName("계좌 출금을 위해 컨트롤러로 전달된 객체를 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_withdraw_request_dto_convert() {
        // given
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder()
                .fullNumber(123456789L)
                .password("1234")
                .amount(10000L)
                .transactionType(TransactionType.WITHDRAW)
                .build();

        // when
        AccountWithdrawRequestServiceDto result = AccountControllerToServiceConverter.accountWithdrawRequestConvert(accountWithdrawRequestDto);

        // then
        assertThat(result.getFullNumber()).isEqualTo(123456789L);
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getAmount()).isEqualTo(10000L);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.WITHDRAW);
    }

    @DisplayName("계좌 출금을 위해 컨트롤러로 전달된 객체를 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void account_transfer_request_dto_convert() {
        // given
        AccountTransferRequestDto accountTransferRequestDto = AccountTransferRequestDto.builder()
                .withdrawFullNumber(123456789L)
                .depositFullNumber(987654321L)
                .withdrawPassword(1234L)
                .amount(10000L)
                .transactionType(TransactionType.TRANSFER)
                .build();

        // when
        AccountTransferRequestServiceDto result = AccountControllerToServiceConverter.accountTransferRequestConvert(accountTransferRequestDto);

        // then
        assertThat(result.getWithdrawFullNumber()).isEqualTo(123456789L);
        assertThat(result.getDepositFullNumber()).isEqualTo(987654321L);
        assertThat(result.getWithdrawPassword()).isEqualTo(1234L);
        assertThat(result.getAmount()).isEqualTo(10000L);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.TRANSFER);
    }
}