package shop.woosung.bank.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.controller.port.UserService;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.dto.JoinResponseDto;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @DisplayName("회원가입에 성공한다.")
    @ParameterizedTest
    @MethodSource("validJoinRequests")
    public void join_success(String email, String password, String name) throws Exception {
        // given
        String requestBody = om.writeValueAsString(JoinRequestDto.builder().email(email).password(password).name(name).build());

        // stub
        when(userService.join(any())).thenReturn(JoinResponseDto.from(User.builder().id(1L).password(password).email(email).name(name).build()));

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("회원가입 성공"));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.email").value(email));
        resultActions.andExpect(jsonPath("$.data.name").value(name));
    }

    @DisplayName("회원가입시 입력 데이터가 잘못되거나 누락되면 에러를 응답한다.")
    @ParameterizedTest
    @MethodSource("invalidJoinRequests")
    public void if_wrong_request_data_when_join_throw_exception(String email, String password, String name) throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
        String requestBody = om.writeValueAsString(joinRequestDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("유효성 검사 실패"));
    }

    private static Stream<Arguments> validJoinRequests() {
        return Stream.of(
                // email
                Arguments.of("a@test.com", "1234", "test1"),
                Arguments.of("aA0._%+-@test.com", "1234", "test1"),
                Arguments.of("abcdefghi@test.com", "1234", "test1"),
                Arguments.of("test1@aA0.-.com", "1234", "test1"),
                Arguments.of("test@test.co.kr", "1234", "test1"),
                // password
                Arguments.of("test1@test.com", "1234", "test1"),
                Arguments.of("test1@test.com", "12345678901234567890", "test1"),
                // name
                Arguments.of("test1@test.com", "1234", "테스"),
                Arguments.of("test1@test.com", "1234", "azAZ가힣09"),
                Arguments.of("test1@test.com", "1234", "가2345678901234567890")
        );
    }

    private static Stream<Arguments> invalidJoinRequests() {
        return Stream.of(
                // email
                Arguments.of("", "1234", "test1"),
                Arguments.of("@test.com", "1234", "test1"),
                Arguments.of("한국어@test.com", "1234", "test1"),
                Arguments.of("abcdefghijklm@test.com", "1234", "test1"),
                Arguments.of("test1@~test.com", "1234", "test1"),
                Arguments.of("test@test", "1234", "test1"),
                Arguments.of("test@test.c", "1234", "test1"),
                Arguments.of("t\uD83D\uDE0A@test.c", "1234", "test1"),
                // password
                Arguments.of("test1@test.com", "", "test1"),
                Arguments.of("test1@test.com", "123", "test1"),
                Arguments.of("test1@test.com", "\uD83D\uDE0A", "test1"),
                Arguments.of("test1@test.com", "123456789012345678901", "test1"),
                // name
                Arguments.of("test1@test.com", "1234", ""),
                Arguments.of("test1@test.com", "1234", "t"),
                Arguments.of("test1@test.com", "1234", "test56789012345678901"),
                Arguments.of("test1@test.com", "1234", "te\uD83D\uDE0A")
        );
    }
}