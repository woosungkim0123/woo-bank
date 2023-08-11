package shop.woosung.bank.util.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.UserRole;

import java.time.LocalDateTime;

public class DummyUserObject {

    protected Transaction newMockTransferTransaction(Long id, Account withdrawAccount, Account depositAccount) {
        depositAccount.deposit(100L);
        return Transaction.builder()
                .id(id)
                .depositAccount(depositAccount)
                .withdrawAccount(withdrawAccount)
                .depositAccountBalance(depositAccount.getBalance())
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber() + "")
                .receiver(depositAccount.getNumber() + "")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account account) {
        account.deposit(100L);
        return Transaction.builder()
                .id(id)
                .depositAccount(account)
                .withdrawAccount(null)
                .depositAccountBalance(account.getBalance())
                .withdrawAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel("01012341234")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockWithdrawTransaction(Long id, Account account) {
        account.withdraw(100L);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(account)
                .withdrawAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .receiver("ATM")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected UserEntity newUser(String name, String password, String email, UserRole role) {
        String encodingPassword = new BCryptPasswordEncoder().encode(password);
        return UserEntity.fromModel(User.builder()
                .name(name)
                .password(encodingPassword)
                .email(email)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    protected UserEntity newMockUser(Long id, String name, String password, String email, String fullname, UserRole role) {
        String encodingPassword = new BCryptPasswordEncoder().encode(password);
        return UserEntity.fromModel(User.builder()
                .id(id)
                .name(name)
                .password(encodingPassword)
                .email(email)
                .role(role)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    protected Account newAccount(Long number, UserEntity userEntity) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(userEntity)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, UserEntity userEntity) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(userEntity)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
