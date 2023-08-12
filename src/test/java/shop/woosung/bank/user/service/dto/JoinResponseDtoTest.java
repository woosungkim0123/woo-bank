package shop.woosung.bank.user.service.dto;

import org.junit.jupiter.api.Test;
import shop.woosung.bank.user.UserRole;
import shop.woosung.bank.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class JoinResponseDtoTest {

    @Test
    public void User_로_유저_응답을_만들_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .role(UserRole.CUSTOMER)
                .build();

        // when
        JoinResponseDto result = JoinResponseDto.from(user);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test1@test.com");
        assertThat(result.getName()).isEqualTo("test1");
    }
}