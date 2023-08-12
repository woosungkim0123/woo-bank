package shop.woosung.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.woosung.bank.dto.account.AccountReqDto.*;


@Sql("classpath:sql/initAutoIncrementReset.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyUserObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        UserEntity test1 = userJpaRepository.save(newUser("test1", "1234", "test1@naver.com", UserRole.CUSTOMER));
        UserEntity test2 = userJpaRepository.save(newUser("test2", "1234", "test2@naver.com", UserRole.CUSTOMER));
        accountRepository.save(newAccount(11111111111L, test1));
        accountRepository.save(newAccount(11111111112L, test2));
        em.clear();
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        // given
        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
        accountRegisterReqDto.setPassword(1234L);
        String requestBody = om.writeValueAsString(accountRegisterReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findUserAccount_test() throws Exception {
        // given
        // when
        ResultActions resultActions = mvc.perform(get("/api/s/accounts"));

        // then
        resultActions.andExpect(status().isOk());
    }
    /*
        junit 테스트에서는 delete 쿼리 다음에 쿼리가 없으면 sql이 안찍힘
     */
    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccountTest() throws Exception {
        // given
        Long accountNumber = 11111111111L;

        // when
        mvc.perform(delete("/api/s/account/" + accountNumber));

        // then
        assertThat(accountRepository.findByNumber(accountNumber)).isEmpty();
    }
    @Test
    public void depositAccount_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(11111111111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setType("DEPOSIT");
        accountDepositReqDto.setTel("01012341234");
        String requestBody = om.writeValueAsString(accountDepositReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/account/deposit")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.data.number").value(11111111111L));
        resultActions.andExpect(jsonPath("$.data.transaction.amount").value(100L));
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void withdrawAccount_test() throws Exception {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(11111111111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setType("WITHDRAW");
        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        System.out.println("requestBody = " + requestBody);
        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.data.number").value(11111111111L));
        resultActions.andExpect(jsonPath("$.data.balance").value(900L));
    }


    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void transferAccount_test() throws Exception {
        // given
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(11111111111L);
        accountTransferReqDto.setDepositNumber(11111111112L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setType("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.data.number").value(11111111111L));
        resultActions.andExpect(jsonPath("$.data.balance").value(900L));
        resultActions.andExpect(jsonPath("$.data.transaction.amount").value(100L));
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findDetailAccount_test() throws Exception {
        // given
        Long number = 11111111111L;
        String type = "ALL";
        String page = "0";

        ResultActions resultActions = mvc.perform(get("/api/s/account/" + number).param("type", type).param("page", page));

        // then
        resultActions.andExpect(status().isOk());
    }
}