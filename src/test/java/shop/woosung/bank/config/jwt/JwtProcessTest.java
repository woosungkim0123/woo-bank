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
    public void verify_test(Long id, UserEnum expectedRole) {

        String jwtToken = createToken(id, expectedRole);
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
                Arguments.of(1L, UserEnum.CUSTOMER),
                Arguments.of(1L, UserEnum.ADMIN)
        );
    }

    private String createToken(Long id, UserEnum role) {
        User user = User.builder()
                .id(id).role(role).build();
        LoginUser loginUser = new LoginUser(user);
        return JwtProcess.create(loginUser).replace(JwtVO.TOKEN_PREFIX, "");
    }
}