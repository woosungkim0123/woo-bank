package shop.woosung.bank.config.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JwtProcessTest {
    
    @DisplayName("jwt create success")
    @Test
    public void create_test() {
        // given
        User user = User.builder()
                .id(1L).role(UserEnum.ADMIN).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        String jwtToken = JwtProcess.create(loginUser);
        
        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
    }

    @DisplayName("jwt 검증 성공")
    @ParameterizedTest(name = "{index} - ID : {1}, 역할 : {2}")
    @MethodSource("jwtTokenAndRoleProvider")
    public void verify_test(String jwtToken, Long id, UserEnum expectedRole) {
        // given & when
        LoginUser loginUser = JwtProcess.verify(jwtToken);

        // then
        assertThat(loginUser.getUser().getId()).isEqualTo(id);
        assertThat(loginUser.getUser().getRole()).isEqualTo(expectedRole);
    }
    /*
        첫번째 : { no : 1, userEnum : CUSTOMER }
        두번째 : { no : 1, userEnum : ADMIN }
     */
    private static Stream<Arguments> jwtTokenAndRoleProvider() {
        return Stream.of(
                Arguments.of("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4OTUxNzA5MX0.Rp8HFFnOFf9SBmJgybSvrHZKM6LusDduVvZ1At9YyBjgIu3buwfI1uSvt7r02CzVmW7Ce-xqCAIzoBumogr8ew", 1L, UserEnum.CUSTOMER),
                Arguments.of("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkFETUlOIiwiaWQiOjEsImV4cCI6MTY4OTUxODMyNX0.RexfQWhxR-WqBq5Wf8UCd5Lx86zCW6DmBd7LGWs9f9oUDt4Yu40cOdBmTlHNjHoT6gkjbe5BxVxWgYpgO_KdXg", 1L, UserEnum.ADMIN)
        );
    }

}