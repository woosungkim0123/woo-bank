package shop.woosung.bank.domain.transaction.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DataJpaTest // db 관련 빈들만 올라옴, 여기선 더티체킹이 되긴하는데 그냥 주는게 나을듯
class TransactionRepositoryImplTest extends DummyUserObject {

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
        autoIncrementReset();
        dataSetting();
        em.clear();
    }

    /*
        FETCH를 지우니까 JOIN은 하지만 PROJECTION이 안됨(투영)
        --> SELECT절에포함을안시킴

     */

    @Test
    public void findTransactionList_all_test() {
        // given
        Long accountId = 1L;

        // when
        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "ALL", 0);

        // then
        assertThat(transactionList.get(3).getDepositAccountBalance()).isEqualTo(800L);
    }



    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE tb_wb_user ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE tb_wb_account ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE tb_wb_transaction ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        UserEntity ssar = userJpaRepository.save(newUser("ssar", "1234","ssar@test.com", UserRole.CUSTOMER));
        UserEntity cos = userJpaRepository.save(newUser("cos", "1234", "cos@test.com",UserRole.CUSTOMER));
        UserEntity love = userJpaRepository.save(newUser("love", "1234", "love@test.com", UserRole.CUSTOMER));
        UserEntity admin = userJpaRepository.save(newUser("admin", "1234", "admin@test.com",UserRole.ADMIN));

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