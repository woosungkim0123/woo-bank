package shop.woosung.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.woosung.bank.dto.user.UserReqDto.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyUserObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    private final String correctUsername = "correctUsername";
    private final String correctPassword = "correctPassword";

    @BeforeEach
    public void setUp() {
       userRepository.save(newUser(correctUsername, correctPassword, "correct@naver.com", "correctFullname", UserEnum.CUSTOMER));
    }

    @DisplayName("로그인 성공")
    @Test
    public void successfulAuthentication() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername(correctUsername);
        loginReqDto.setPassword(correctPassword);

        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.message").value("로그인 성공"));
        resultActions.andExpect(jsonPath("$.data.username").value(correctUsername));
    }

    @DisplayName("로그인 실패 - 아이디 불일치")
    @ParameterizedTest(name = "{index} username={0}")
    @ValueSource(strings = {"", "wrongUsername", " " + correctUsername, correctUsername + " "})
    public void wrongUsernameUnsuccessfulAuthentication(String username) throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername(username);
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @ParameterizedTest(name = "{index} password={0}")
    @ValueSource(strings = {"", "wrongPassword", " " + correctPassword, correctPassword + " "})
    public void wrongPasswordUnsuccessfulAuthentication(String password) throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername(correctUsername);
        loginReqDto.setPassword(password);

        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }
}