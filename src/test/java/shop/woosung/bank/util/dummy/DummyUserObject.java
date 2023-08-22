package shop.woosung.bank.util.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.woosung.bank.account.infrastructure.AccountEntity;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;

import java.time.LocalDateTime;

public class DummyUserObject {

    protected Transaction newMockTransferTransaction(Long id, AccountEntity withdrawAccountEntity, AccountEntity depositAccountEntity) {
        depositAccountEntity.deposit(100L);
        return Transaction.builder()
                .id(id)
                .depositAccount(depositAccountEntity)
                .withdrawAccount(withdrawAccountEntity)
                .depositAccountBalance(depositAccountEntity.getBalance())
                .withdrawAccountBalance(withdrawAccountEntity.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccountEntity.getNumber() + "")
                .receiver(depositAccountEntity.getNumber() + "")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, AccountEntity accountEntity) {
        accountEntity.deposit(100L);
        return Transaction.builder()
                .id(id)
                .depositAccount(accountEntity)
                .withdrawAccount(null)
                .depositAccountBalance(accountEntity.getBalance())
                .withdrawAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(accountEntity.getNumber() + "")
                .tel("01012341234")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockWithdrawTransaction(Long id, AccountEntity accountEntity) {
        accountEntity.withdraw(100L);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(accountEntity)
                .withdrawAccountBalance(accountEntity.getBalance())
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

    protected AccountEntity newAccount(Long number, UserEntity userEntity) {
        return AccountEntity.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(userEntity)
                .build();
    }

    protected AccountEntity newMockAccount(Long id, Long number, Long balance, UserEntity userEntity) {
        return AccountEntity.builder()
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
