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
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.mock.FakeAccountRepository;
import shop.woosung.bank.mock.FakeUserRepository;
import shop.woosung.bank.mock.config.FakeRepositoryConfiguration;
import shop.woosung.bank.user.domain.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({ FakeRepositoryConfiguration.class })
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FakeUserRepository userRepository;

    @Autowired
    private FakeAccountRepository accountRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        userRepository.save(User.builder().email("test1@test.com").name("test1").build());
        userRepository.save(User.builder().email("test2@test.com").name("test2").build());
    }

    @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 로그인이_된_상태에서_자신의_계좌_리스트를_요청하면_정상적으로_응답한다() throws Exception {
        // given
        User user1 = userRepository.findByEmail("test1@test.com").get();
        User user2 = userRepository.findByEmail("test2@test.com").get();
        accountRepository.save(Account.builder().number(1111111111L).balance(1000L).user(user1).build());
        accountRepository.save(Account.builder().number(1111111112L).balance(2000L).user(user1).build());
        accountRepository.save(Account.builder().number(1111111113L).balance(3000L).user(user2).build());

        // when
        ResultActions resultActions = mvc.perform(
                get("/api/s/accounts"));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("요청 성공"));
        resultActions.andExpect(jsonPath("$.data.accounts.length()").value(2));
        resultActions.andExpect(jsonPath("$.data.accounts[0].number").value(1111111111L));
        resultActions.andExpect(jsonPath("$.data.accounts[0].balance").value(1000L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].number").value(1111111112L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].balance").value(2000L));
    }
}