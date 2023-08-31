package shop.woosung.bank.user.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

import static org.assertj.core.api.Assertions.assertThat;


class UserControllerToServiceConverterTest {

    @DisplayName("클라이언트에서 컨트롤러로 전달한 데이터를 서비스에 전달하기 전 알맞게 새로운 객체로 변환한다.")
    @Test
    void joinRequestDtoConvert() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .build();

        // when
        JoinRequestServiceDto result = UserControllerToServiceConverter.joinRequestConvert(joinRequestDto);

        // then
        assertThat(result.getEmail()).isEqualTo("test1@test.com");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getName()).isEqualTo("test1");
    }
}