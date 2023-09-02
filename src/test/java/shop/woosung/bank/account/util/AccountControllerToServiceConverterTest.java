package shop.woosung.bank.account.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;

import static org.assertj.core.api.Assertions.assertThat;

class AccountControllerToServiceConverterTest {

    @DisplayName("계좌 등록을 위해 컨트롤러로 전달된 객체를 서비스로 전달하기 위해 알맞게 변환한다.")
    @Test
    void accountRegisterRequestDtoConvert() {
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
}