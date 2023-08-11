package shop.woosung.bank.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.domain.transaction.repository.TransactionRepository;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest extends DummyUserObject {

    @Autowired private MockMvc mvc;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
        em.clear();
    }
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findTransactionList_test() throws Exception {
        // given
        Long number = 1111L;
        String type = "ALL";
        String page = "0";

        ResultActions resultActions = mvc.perform(get("/api/s/account/" + number + "/transaction").param("type", type).param("page", page));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
        resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
        resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
        resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
    }



    private void dataSetting() {
        UserEntity ssar = userJpaRepository.save(newUser("ssar", "1234","ssar@test.com", UserRole.CUSTOMER));
        UserEntity cos = userJpaRepository.save(newUser("cos", "1234", "cos@test.com",UserRole.CUSTOMER));
        UserEntity love = userJpaRepository.save(newUser("love", "1234", "love@test.com", UserRole.CUSTOMER));
        UserEntity admin = userJpaRepository.save(newUser("admin", "1234", "admin@test.com", UserRole.ADMIN));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        transactionRepository.save(makeWithdrawTransaction(ssarAccount1, 100L));
        transactionRepository.save(makeDepositTransaction(cosAccount, 100L));
        transactionRepository.save(makeTransferTransaction(ssarAccount1, cosAccount, 100L));
        transactionRepository.save(makeTransferTransaction(ssarAccount1, loveAccount, 100L));
        transactionRepository.save(makeTransferTransaction(cosAccount, ssarAccount1, 100L));
    }

    private Transaction makeDepositTransaction(Account account, long amount) {
        account.deposit(amount);
        accountRepository.save(account);
        return Transaction.builder()
                .depositAccount(account)
                .depositAccountBalance(account.getBalance())
                .amount(amount)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(Long.toString(account.getNumber()))
                .tel("01012341234")
                .build();
    }

    private Transaction makeWithdrawTransaction(Account account, long amount) {
        account.withdraw(amount);
        accountRepository.save(account);
        return Transaction.builder()
                .withdrawAccount(account)
                .withdrawAccountBalance(account.getBalance())
                .amount(amount)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(Long.toString(account.getNumber()))
                .receiver("ATM")
                .build();
    }

    private Transaction makeTransferTransaction(Account withdrawAccount, Account depositAccount, long amount) {
        withdrawAccount.withdraw(amount);
        depositAccount.deposit(amount);
        accountRepository.save(withdrawAccount);
        accountRepository.save(depositAccount);
        return Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(amount)
                .gubun(TransactionEnum.TRANSFER)
                .sender(Long.toString(withdrawAccount.getNumber()))
                .receiver(Long.toString(depositAccount.getNumber()))
                .build();
    }
}