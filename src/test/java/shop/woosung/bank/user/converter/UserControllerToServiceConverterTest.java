package shop.woosung.bank.user.converter;

import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

import static org.assertj.core.api.Assertions.assertThat;


class UserControllerToServiceConverterTest {

    @Test
    void 컨버터는_서비스에게_전달할_목적으로_컨트롤러에서_받은_데이터를_새로운_객체로_변환_한다() {
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