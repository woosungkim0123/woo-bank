package shop.woosung.bank.user.converter;

import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.domain.UserCreate;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceToDomainConverterTest {

    @Test
    public void 컨버터는_도메인에_전달할_목적으로_서비스의_데이터를_도메인_전용_객체로_변환_한다() {
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