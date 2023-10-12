package shop.woosung.bank.config.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.config.auth.exception.JwtExpiredException;
import shop.woosung.bank.config.auth.exception.JwtIdConversionException;
import shop.woosung.bank.config.auth.exception.JwtNotHaveIdException;
import shop.woosung.bank.config.auth.exception.JwtVerifyException;
import shop.woosung.bank.config.auth.jwt.JwtTokenProvider;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserRepository userRepository;

    @DisplayName("올바른 토큰을 가지고 요청시 권한이 필요한 접근에 성공한다.")
    @Test
    void if_have_correct_token_access_success() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "correctToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenReturn(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).email("test@test.com").password("password").name("test").role(UserRole.CUSTOMER).build()));

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("권한이 필요할 때 Header Key 값이 없다면 에러를 응답한다.")
    @Test
    void if_header_not_have_token_throw_exception() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/api/s/test"));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("로그인 필요"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("잘못된 토큰을 가지고 요청시 에러를 응답한다.")
    @Test
    void if_have_wrong_token_throw_exception() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "wrongToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenThrow(JwtVerifyException.class);

        // when
        ResultActions result = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 실패"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("만료된 토큰을 가지고 요청시 에러를 응답한다.")
    @Test
    void if_have_expired_token_throw_exception() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "wrongToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenThrow(JwtExpiredException.class);

        // when
        ResultActions result = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 만료"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("해독된 토큰에 id가 없으면 에러를 응답한다.")
    @Test
    void if_verify_token_not_have_id_throw_exception() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "wrongToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenThrow(JwtNotHaveIdException.class);

        // when
        ResultActions result = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 실패"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("해독된 토큰에 id가 없으면 에러를 응답한다.")
    @Test
    void if_verify_token_not_convert_id_throw_exception() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "wrongToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenThrow(JwtIdConversionException.class);

        // when
        ResultActions result = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 실패"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("검증시 유저를 찾을 수 없다면 에러를 응답한다.")
    @Test
    void if_not_found_user_when_authorization_throw_exception() throws Exception {
        // given
        String requestToken = JwtVO.TOKEN_PREFIX + "wrongToken";

        // stub
        when(jwtTokenProvider.verify(anyString())).thenReturn(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        ResultActions result = mvc.perform(get("/api/s/test")
                .header(JwtVO.HEADER, requestToken));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 실패"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }
}