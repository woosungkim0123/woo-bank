package shop.woosung.bank.config.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JwtProcessTest {
    
    @DisplayName("jwt create success")
    @Test
    public void create_test() {
        // given
        User userEntity = User.builder()
                .id(1L).role(UserRole.ADMIN).build();
        LoginUser loginUser = new LoginUser(userEntity);
        JwtTestTokenProvider jwtTestHolder = new JwtTestTokenProvider();

        // when
        String jwtToken = JwtProcess.create(jwtTestHolder, loginUser);
        
        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
    }

    @DisplayName("jwt 검증 성공")
    @ParameterizedTest(name = "{index} - ID : {1}, 역할 : {2}")
    @MethodSource("jwtTokenAndRoleProvider")
    public void verify_test(Long id, UserRole expectedRole) {

        String jwtToken = createToken(id, expectedRole);
        JwtTestTokenProvider jwtTestHolder = new JwtTestTokenProvider();
        // given & when
        LoginUser loginUser = JwtProcess.verify(jwtTestHolder, jwtToken);

        // then
        assertThat(loginUser.getUser().getRole()).isEqualTo(expectedRole);
    }
    /*
        첫번째 : { no : 1, userEnum : CUSTOMER }
        두번째 : { no : 1, userEnum : ADMIN }
     */
    private static Stream<Arguments> jwtTokenAndRoleProvider() {
        return Stream.of(
                Arguments.of(1L, UserRole.CUSTOMER),
                Arguments.of(1L, UserRole.ADMIN)
        );
    }

    private String createToken(Long id, UserRole role) {
        User userEntity = User.builder()
                .id(id).role(role).build();
        LoginUser loginUser = new LoginUser(userEntity);
        JwtTestTokenProvider jwtTestHolder = new JwtTestTokenProvider();
        return JwtProcess.create(jwtTestHolder, loginUser).replace(JwtVO.TOKEN_PREFIX, "");
    }
}