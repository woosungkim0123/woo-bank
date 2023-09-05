package shop.woosung.bank.user.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.domain.UserCreate;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceToDomainConverterTest {

    @DisplayName("회원가입을 위해 서비스의 데이터를 회원가입 도메인 전용 객체로 변환한다.")
    @Test
    public void userCreateConvert_test() {
        // given
        JoinRequestServiceDto joinRequestServiceDto = JoinRequestServiceDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();
        UserRole role = UserRole.CUSTOMER;

        // when
        UserCreate result = UserServiceToDomainConverter.userCreateConvert(joinRequestServiceDto, role);

        // then
        assertThat(result.getEmail()).isEqualTo("test1@test.com");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getName()).isEqualTo("test1");
        assertThat(result.getRole()).isEqualTo(UserRole.CUSTOMER);
    }
}