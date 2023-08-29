package shop.woosung.bank.account.infrastructure;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.user.infrastructure.UserEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountSequenceEntity {

    @Id
    private String sequenceName;
    private Long nextValue;
    private Long incrementBy;

    public static AccountSequenceEntity fromModel(AccountSequence accountSequence) {
        AccountSequenceEntity accountSequenceEntity = new AccountSequenceEntity();
        accountSequenceEntity.sequenceName = accountSequence.getSequenceName().name();
        accountSequenceEntity.nextValue = accountSequence.getNextValue();
        accountSequenceEntity.incrementBy = accountSequence.getIncrementBy();
        return accountSequenceEntity;
    }

    public AccountSequence toModel() {
        return AccountSequence.builder()
                .sequenceName(AccountType.valueOf(sequenceName))
                .nextValue(nextValue)
                .incrementBy(incrementBy)
                .build();
    }
}
