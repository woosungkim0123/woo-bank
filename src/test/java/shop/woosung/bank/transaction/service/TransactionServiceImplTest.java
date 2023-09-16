package shop.woosung.bank.transaction.service;

import org.junit.jupiter.api.BeforeEach;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.service.AccountServiceImpl;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.mock.repository.*;
import shop.woosung.bank.mock.util.FakePasswordEncoder;
import shop.woosung.bank.transaction.controller.port.TransactionService;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    private TransactionService transactionService;
    private FakeAccountRepository accountRepository;
    private FakeTransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        accountRepository = new FakeAccountRepository();
        transactionRepository = new FakeTransactionRepository();
        this.transactionService = TransactionServiceImpl.builder()
                .accountRepository(accountRepository)
                .transactionRepository(transactionRepository)
                .build();

        User user1 = User.builder().id(1L).name("test1").build();
        Account account = Account.builder()
                        .number(1111L)
                        .fullnumber(2321111L)
                        .password("1234")
                        .balance(1000L)
                        .type(AccountType.NORMAL)
                        .user(user1)
                        .build();
        accountRepository.save(account);

    }
}