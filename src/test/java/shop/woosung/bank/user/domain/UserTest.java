package shop.woosung.bank.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woosung.bank.mock.util.FakePasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("회원가입을 위한 User를 생성한다.")
    @Test
    void join_test() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .role(UserRole.CUSTOMER)
                .build();

        // when
        User user = User.join(userCreate, new FakePasswordEncoder("aaaa_bbbb_cccc_dddd"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test1@test.com");
        assertThat(user.getPassword()).isEqualTo("aaaa_bbbb_cccc_dddd");
        assertThat(user.getName()).isEqualTo("test1");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }
}
