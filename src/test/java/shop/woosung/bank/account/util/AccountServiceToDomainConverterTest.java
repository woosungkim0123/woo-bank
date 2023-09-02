package shop.woosung.bank.account.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.account.domain.AccountRegister;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceToDomainConverterTest {

    @DisplayName("계좌 등록을 위해 서비스의 데이터를 회원가입 도메인 전용 객체로 변환한다.")
    @Test
    public void userCreateConvert_test() {
        // given
        AccountRegisterRequestServiceDto accountRegisterRequestServiceDto = AccountRegisterRequestServiceDto.builder()
                .password("1234")
                .balance(10000)
                .type(AccountType.NORMAL)
                .build();
        User user = User.builder().id(1L).name("test").build();

        // when
        AccountRegister result = AccountServiceToDomainConverter.accountRegisterConvert(accountRegisterRequestServiceDto, 1L, 1L, user);

        // then
        assertThat(result.getTypeNumber()).isEqualTo(1L);
        assertThat(result.getNewNumber()).isEqualTo(1L);
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getBalance()).isEqualTo(10000);
        assertThat(result.getAccountType()).isEqualTo(AccountType.NORMAL);
        assertThat(result.getUser()).isEqualTo(user);
    }
}