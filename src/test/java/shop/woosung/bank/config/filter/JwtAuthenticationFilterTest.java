package shop.woosung.bank.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.config.auth.dto.LoginRequestDto;
import shop.woosung.bank.config.auth.jwt.JwtVO;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.service.port.UserRepository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String correctEmail = "test@test.com";
    private static final String correctPassword = "1234";
    private static final String wrongEmail = "wrong@test.com";
    private static final String wrongPassword = "12345";

    @BeforeEach
    public void setting() {
        User user = User.builder().email(correctEmail).password(passwordEncoder.encode(correctPassword)).name("test").role(UserRole.CUSTOMER).build();
        userRepository.save(user);
    }

    @Test
    public void 로그인_성공() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email(correctEmail).password(correctPassword).build();
        String requestBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("로그인 완료"));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.name").value("test"));
        resultActions.andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }

    @ParameterizedTest()
    @MethodSource("notExistUsers")
    public void 로그인_실패_잘못된_계정(String email, String password) throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email(email).password(password).build();
        String requestBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("계정 정보를 확인 해주세요."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @ParameterizedTest()
    @MethodSource("invalidLoginRequestDto")
    public void 로그인_실패_요청_오류(String email, String password) throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email(email).password(password).build();
        String requestBody = om.writeValueAsString(loginRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("유효하지 않은 요청 입니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    private static Stream<Arguments> notExistUsers() {
        return Stream.of(
                // email
                Arguments.of(correctEmail + " ", correctPassword),
                Arguments.of(" " + correctEmail, correctPassword),
                Arguments.of(wrongEmail, correctPassword),
                Arguments.of(" ", correctPassword),
                // password
                Arguments.of(correctEmail, wrongPassword),
                Arguments.of(correctEmail, " " + correctPassword),
                Arguments.of(correctEmail, correctPassword + " "),
                Arguments.of(correctEmail, " "),

                // both
                Arguments.of(wrongEmail, wrongPassword),
                Arguments.of(" " + correctEmail, " " + correctPassword),
                Arguments.of(" " + correctEmail, correctPassword + " "),
                Arguments.of(correctEmail + " ", " " + correctPassword),
                Arguments.of(correctEmail + " ",  correctPassword + " "),
                Arguments.of(" ", " ")
        );
    }

    private static Stream<Arguments> invalidLoginRequestDto() {
        return Stream.of(
                // email
                Arguments.of(null, correctPassword),
                Arguments.of("", correctPassword),
                // password
                Arguments.of(correctEmail, null),
                Arguments.of(correctEmail, ""),
                // both
                Arguments.of(null, null),
                Arguments.of("", "")
        );
    }
}