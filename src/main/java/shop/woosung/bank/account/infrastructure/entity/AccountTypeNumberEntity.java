package shop.woosung.bank.account.infrastructure.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountTypeNumberEntity extends BaseTimeEntity {

    @Id
    private String accountType;
    @Column(unique = true, nullable = false)
    private Long number;

    public static AccountTypeNumberEntity fromModel(AccountTypeNumber accountTypeNumber) {
        AccountTypeNumberEntity accountSequenceEntity = new AccountTypeNumberEntity();
        accountSequenceEntity.accountType = accountTypeNumber.getAccountType().name();
        accountSequenceEntity.number = accountTypeNumber.getNumber();
        return accountSequenceEntity;
    }

    public AccountTypeNumber toModel() {
        return AccountTypeNumber.builder()
                .accountType(AccountType.valueOf(accountType))
                .number(number)
                .build();
    }
}
