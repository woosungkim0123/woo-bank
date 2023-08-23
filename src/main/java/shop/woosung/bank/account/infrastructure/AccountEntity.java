package shop.woosung.bank.account.infrastructure;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;
import shop.woosung.bank.user.infrastructure.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_wb_account")
@Entity
public class AccountEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private Long number;

    @Column(nullable = false, length = 4)
    private Long password;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    public static AccountEntity fromModel(Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.id = account.getId();
        accountEntity.number = account.getNumber();
        accountEntity.password = account.getPassword();
        accountEntity.balance = account.getBalance();
        accountEntity.user = UserEntity.fromModel(account.getUser());
        return accountEntity;
    }

    public Account toModel() {
        return Account.builder()
                .id(id)
                .number(number)
                .password(password)
                .balance(balance)
                .user(user.toModel())
                .build();
    }
}