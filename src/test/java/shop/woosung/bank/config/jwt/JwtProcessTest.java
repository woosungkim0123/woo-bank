package shop.woosung.bank.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.exception.JwtIdConversionException;
import shop.woosung.bank.config.auth.exception.JwtNotHaveIdException;
import shop.woosung.bank.config.auth.exception.JwtVerifyException;
import shop.woosung.bank.config.auth.jwt.JwtProcess;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtProcessTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("정상적인 토큰을 생성 합니다.")
    @Test
    void create_correct_token() {
        // given
        LoginUser loginUser = new LoginUser(User.builder().id(1L).role(UserRole.ADMIN).build());

        // stub
        when(jwtTokenProvider.create(any())).thenReturn("correctToken");

        // when
        String jwtToken = JwtProcess.create(jwtTokenProvider, loginUser);
        
        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
        assertThat(jwtToken).endsWith("correctToken");
    }

    @DisplayName("정상적인 토큰을 검증 합니다.")
    @Test
    void verify_correct_token() {
        // given
        String correctToken = "correctToken";

        // stub
        when(jwtTokenProvider.verify(any())).thenReturn(1L);

        // when
        Long userId = JwtProcess.verify(jwtTokenProvider, correctToken);

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @DisplayName("잘못된 토큰을 검증하면 예외를 던진다.")
    @Test
    void verify_wrong_token_throw_exception() {
        // given
        String wrongToken = "wrongToken";

        // stub
        when(jwtTokenProvider.verify(any())).thenThrow(JwtVerifyException.class);

        // when & then
        assertThatThrownBy(() -> JwtProcess.verify(jwtTokenProvider, wrongToken))
                .isInstanceOf(JwtVerifyException.class);
    }

    @DisplayName("만료된 토큰을 검증하면 예외를 던진다.")
    @Test
    void verify_expired_token_throw_exception() {
        // given
        String wrongToken = "expiredToken";

        // stub
        when(jwtTokenProvider.verify(any())).thenThrow(TokenExpiredException.class);

        // when & then
        assertThatThrownBy(() -> JwtProcess.verify(jwtTokenProvider, wrongToken))
                .isInstanceOf(TokenExpiredException.class);
    }

    @DisplayName("검증된 토큰에 Id가 없으면 예외를 던진다.")
    @Test
    void if_verify_token_not_have_id_throw_exception() {
        // given
        String wrongToken = "expiredToken";

        // stub
        when(jwtTokenProvider.verify(any())).thenThrow(JwtNotHaveIdException.class);

        // when & then
        assertThatThrownBy(() -> JwtProcess.verify(jwtTokenProvider, wrongToken))
                .isInstanceOf(JwtNotHaveIdException.class);
    }

    @DisplayName("토큰의 Id를 숫자로 변환할 수 없으면 예외를 던진다.")
    @Test
    void if_not_convert_id_throw_exception() {
        // given
        String wrongToken = "wrongToken";

        // stub
        when(jwtTokenProvider.verify(any())).thenThrow(JwtIdConversionException.class);

        // when & then
        assertThatThrownBy(() -> JwtProcess.verify(jwtTokenProvider, wrongToken))
                .isInstanceOf(JwtIdConversionException.class);
    }
}