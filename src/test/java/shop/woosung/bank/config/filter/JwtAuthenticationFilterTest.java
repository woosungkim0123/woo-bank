package shop.woosung.bank.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.config.auth.LoginService;
import shop.woosung.bank.config.auth.LoginUser;
import shop.woosung.bank.config.auth.dto.LoginRequestDto;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private LoginService loginService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("알맞은 형식으로 로그인 요청시 로그인에 성공한다.")
    @Test
    void login_success() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("test@test.com").password("password").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // stub
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("test@test.com", "password"));
        when(loginService.loadUserByUsername(any())).thenReturn(new LoginUser(User.builder().id(1L).email("test@test.com").password("password").name("test").role(UserRole.CUSTOMER).build()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isOk());
        result.andExpect(header().string("Authorization", org.hamcrest.Matchers.startsWith("Bearer ")));
        result.andExpect(jsonPath("$.status").value("success"));
        result.andExpect(jsonPath("$.message").value("로그인 완료"));
        result.andExpect(jsonPath("$.data.id").value(1L));
        result.andExpect(jsonPath("$.data.name").value("test"));
        result.andExpect(jsonPath("$.data.role").value(UserRole.CUSTOMER.name()));
    }

    @DisplayName("로그인 요청시 이메일이 없으면 에러를 응답한다.")
    @Test
    void if_request_not_have_email_when_login_response_error() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().password("password").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("유효하지 않은 요청"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인 요청시 패스워드가 없으면 에러를 응답한다.")
    @Test
    void if_request_not_have_password_when_login_response_error() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("test@test.com").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("유효하지 않은 요청"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인 요청시 이메일에 공백이 있으면 에러를 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = { " test@test.com", "test@test.com " })
    void if_request_email_have_white_space_when_login_response_error(String email) throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email(email).password("password").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("유효하지 않은 요청"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인 요청시 이메일에 맞는 유저가 없으면 에러를 응답한다.")
    @Test
    void if_request_email_matched_user_when_login_response_error() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("test@test.com").password("password").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // stub
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("test@test.com", "password"));
        when(loginService.loadUserByUsername(any())).thenThrow(InternalAuthenticationServiceException.class);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("계정 확인 필요"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("로그인 요청시 비밀번호가 틀리면 에러를 응답한다.")
    @Test
    void if_request_password_wrong_when_login_response_error() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("test@test.com").password("password").build();
        String responseBody = om.writeValueAsString(loginRequestDto);

        // stub
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("test@test.com", "password"));
        when(loginService.loadUserByUsername(any())).thenReturn(new LoginUser(User.builder().id(1L).email("test@test.com").password("password").name("test").role(UserRole.CUSTOMER).build()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseBody));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("계정 확인 필요"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }
}