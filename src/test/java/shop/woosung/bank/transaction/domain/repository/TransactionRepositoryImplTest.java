//package shop.woosung.bank.transaction.domain.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
//import shop.woosung.bank.account.infrastructure.AccountJpaRepository;
//import shop.woosung.bank.transaction.domain.Transaction;
//import shop.woosung.bank.transaction.domain.TransactionEnum;
//import shop.woosung.bank.user.infrastructure.UserEntity;
//import shop.woosung.bank.user.domain.UserRole;
//import shop.woosung.bank.user.infrastructure.UserJpaRepository;
//import shop.woosung.bank.util.dummy.DummyUserObject;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@ActiveProfiles("test")
//@DataJpaTest // db 관련 빈들만 올라옴, 여기선 더티체킹이 되긴하는데 그냥 주는게 나을듯
//class TransactionRepositoryImplTest extends DummyUserObject {
//
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
//        autoIncrementReset();
//        dataSetting();
//        em.clear();
//    }
//
//    /*
//        FETCH를 지우니까 JOIN은 하지만 PROJECTION이 안됨(투영)
//        --> SELECT절에포함을안시킴
//
//     */
//
//    @Test
//    public void findTransactionList_all_test() {
//        // given
//        Long accountId = 1L;
//
//        // when
//        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "ALL", 0);
//
//        // then
//        assertThat(transactionList.get(3).getDepositAccountBalance()).isEqualTo(800L);
//    }
//
//
//
//    private void autoIncrementReset() {
//        em.createNativeQuery("ALTER TABLE tb_wb_user ALTER COLUMN id RESTART WITH 1").executeUpdate();
//        em.createNativeQuery("ALTER TABLE tb_wb_account ALTER COLUMN id RESTART WITH 1").executeUpdate();
//        em.createNativeQuery("ALTER TABLE tb_wb_transaction ALTER COLUMN id RESTART WITH 1").executeUpdate();
//    }
//
//    private void dataSetting() {
//        UserEntity ssar = userJpaRepository.save(newUser("ssar", "1234","ssar@test.com", UserRole.CUSTOMER));
//        UserEntity cos = userJpaRepository.save(newUser("cos", "1234", "cos@test.com",UserRole.CUSTOMER));
//        UserEntity love = userJpaRepository.save(newUser("love", "1234", "love@test.com", UserRole.CUSTOMER));
//        UserEntity admin = userJpaRepository.save(newUser("admin", "1234", "admin@test.com",UserRole.ADMIN));
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
//
//}