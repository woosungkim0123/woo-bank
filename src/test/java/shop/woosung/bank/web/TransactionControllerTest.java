//package shop.woosung.bank.web;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.TestExecutionEvent;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
//import shop.woosung.bank.account.infrastructure.AccountJpaRepository;
//import shop.woosung.bank.transaction.domain.Transaction;
//import shop.woosung.bank.transaction.domain.TransactionEnum;
//import shop.woosung.bank.transaction.domain.TransactionRepository;
//import shop.woosung.bank.user.infrastructure.UserEntity;
//import shop.woosung.bank.user.domain.UserRole;
//import shop.woosung.bank.user.infrastructure.UserJpaRepository;
//import shop.woosung.bank.util.dummy.DummyUserObject;
//
//import javax.persistence.EntityManager;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Sql("classpath:sql/initAutoIncrementReset.sql")
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class TransactionControllerTest extends DummyUserObject {
//
//    @Autowired private MockMvc mvc;
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//    @Autowired
//    private AccountJpaRepository accountJpaRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private EntityManager em;
//
//    @BeforeEach
//    public void setUp() {
//        dataSetting();
//        em.clear();
//    }
//    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @Test
//    public void findTransactionList_test() throws Exception {
//        // given
//        Long number = 1111L;
//        String type = "ALL";
//        String page = "0";
//
//        ResultActions resultActions = mvc.perform(get("/api/s/account/" + number + "/transaction").param("type", type).param("page", page));
//
//        // then
//        resultActions.andExpect(status().isOk());
//        resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
//        resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
//        resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
//        resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
//    }
//
//
//
//    private void dataSetting() {
//        UserEntity ssar = userJpaRepository.save(newUser("ssar", "1234","ssar@test.com", UserRole.CUSTOMER));
//        UserEntity cos = userJpaRepository.save(newUser("cos", "1234", "cos@test.com",UserRole.CUSTOMER));
//        UserEntity love = userJpaRepository.save(newUser("love", "1234", "love@test.com", UserRole.CUSTOMER));
//        UserEntity admin = userJpaRepository.save(newUser("admin", "1234", "admin@test.com", UserRole.ADMIN));
//
//        AccountEntity ssarAccount1Entity = accountJpaRepository.save(newAccount(1111L, ssar));
//        AccountEntity cosAccountEntity = accountJpaRepository.save(newAccount(2222L, cos));
//        AccountEntity loveAccountEntity = accountJpaRepository.save(newAccount(3333L, love));
//        AccountEntity ssarAccount2Entity = accountJpaRepository.save(newAccount(4444L, ssar));
//
//        transactionRepository.save(makeWithdrawTransaction(ssarAccount1Entity, 100L));
//        transactionRepository.save(makeDepositTransaction(cosAccountEntity, 100L));
//        transactionRepository.save(makeTransferTransaction(ssarAccount1Entity, cosAccountEntity, 100L));
//        transactionRepository.save(makeTransferTransaction(ssarAccount1Entity, loveAccountEntity, 100L));
//        transactionRepository.save(makeTransferTransaction(cosAccountEntity, ssarAccount1Entity, 100L));
//    }
//
//    private Transaction makeDepositTransaction(AccountEntity accountEntity, long amount) {
//        accountEntity.deposit(amount);
//        accountJpaRepository.save(accountEntity);
//        return Transaction.builder()
//                .depositAccount(accountEntity)
//                .depositAccountBalance(accountEntity.getBalance())
//                .amount(amount)
//                .gubun(TransactionEnum.DEPOSIT)
//                .sender("ATM")
//                .receiver(Long.toString(accountEntity.getNumber()))
//                .tel("01012341234")
//                .build();
//    }
//
//    private Transaction makeWithdrawTransaction(AccountEntity accountEntity, long amount) {
//        accountEntity.withdraw(amount);
//        accountJpaRepository.save(accountEntity);
//        return Transaction.builder()
//                .withdrawAccount(accountEntity)
//                .withdrawAccountBalance(accountEntity.getBalance())
//                .amount(amount)
//                .gubun(TransactionEnum.WITHDRAW)
//                .sender(Long.toString(accountEntity.getNumber()))
//                .receiver("ATM")
//                .build();
//    }
//
//    private Transaction makeTransferTransaction(AccountEntity withdrawAccountEntity, AccountEntity depositAccountEntity, long amount) {
//        withdrawAccountEntity.withdraw(amount);
//        depositAccountEntity.deposit(amount);
//        accountJpaRepository.save(withdrawAccountEntity);
//        accountJpaRepository.save(depositAccountEntity);
//        return Transaction.builder()
//                .withdrawAccount(withdrawAccountEntity)
//                .depositAccount(depositAccountEntity)
//                .withdrawAccountBalance(withdrawAccountEntity.getBalance())
//                .depositAccountBalance(depositAccountEntity.getBalance())
//                .amount(amount)
//                .gubun(TransactionEnum.TRANSFER)
//                .sender(Long.toString(withdrawAccountEntity.getNumber()))
//                .receiver(Long.toString(depositAccountEntity.getNumber()))
//                .build();
//    }
//}