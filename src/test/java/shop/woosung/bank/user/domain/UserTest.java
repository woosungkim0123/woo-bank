package shop.woosung.bank.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.common.service.port.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원가입을 위한 User를 생성한다.")
    @Test
    void create_user_for_join() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("test1@test.com")
                .password("1234")
                .name("test1")
                .role(UserRole.CUSTOMER)
                .build();

        // stub
        when(passwordEncoder.encode(any())).thenReturn("aaaa_bbbb_cccc_dddd");

        // when
        User user = User.join(userCreate, passwordEncoder);

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test1@test.com");
        assertThat(user.getPassword()).isEqualTo("aaaa_bbbb_cccc_dddd");
        assertThat(user.getName()).isEqualTo("test1");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }
}
