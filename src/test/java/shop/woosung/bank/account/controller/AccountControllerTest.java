package shop.woosung.bank.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.mock.config.FakeJwtConfiguration;
import shop.woosung.bank.mock.config.FakeRepositoryConfiguration;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({ FakeRepositoryConfiguration.class })
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest {

    @Autowired
    private FakeUserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        userRepository.save(User.builder().email("test@test.com").name("test").build());
    }

    @WithUserDetails(value = "test@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 로그인이_된_상태에서_자신의_계좌_리스트를_요청하면_정상적으로_응답한다() throws Exception {
        // given

//       /* JoinRequestDto joinRequestDto = JoinRequestDto.builder()
//                .email(email)
//                .password(password)
//                .name(name)
//                .build();
//        String requestBody = om.writeValueAsString(joinRequestDto);
//
//        // when
//        ResultActions resultActions = mvc.perform(
//                post("/api/join")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        resultActions.andExpect(status().isCreated());
//        resultActions.andExpect(jsonPath("$.status").value("success"));
//        resultActions.andExpect(jsonPath("$.message").value("회원가입 성공"));
//        resultActions.andExpect(jsonPath("$.data.id").value(1L));
//        resultActions.andExpect(jsonPath("$.data.email").value(email));
//        resultActions.andExpect(jsonPath("$.data.name").value(name));*/
    }
}