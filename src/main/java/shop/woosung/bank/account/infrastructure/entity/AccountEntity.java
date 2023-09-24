package shop.woosung.bank.account.infrastructure.entity;


import javax.persistence.*;

import lombok.AccessLevel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;
import shop.woosung.bank.user.infrastructure.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class AccountEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private Long number;

    @Column(unique = true, nullable = false, length = 20)
    private Long fullNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static AccountEntity fromModel(Account account) {
        if (account == null) return null;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.id = account.getId();
        accountEntity.number = account.getNumber();
        accountEntity.fullNumber = account.getFullNumber();
        accountEntity.password = account.getPassword();
        accountEntity.balance = account.getBalance();
        accountEntity.type = account.getType();
        accountEntity.user = UserEntity.fromModel(account.getUser());
        accountEntity.createdAt = account.getCreatedAt();
        accountEntity.updatedAt = account.getUpdatedAt();
        return accountEntity;
    }

    public Account toModel() {
        return Account.builder()
                .id(id)
                .number(number)
                .fullNumber(fullNumber)
                .password(password)
                .balance(balance)
                .type(type)
                .user(user.toModel())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
