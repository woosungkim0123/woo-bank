package shop.woosung.bank.account.infrastructure.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountSequenceEntity {

    @Id
    private String sequenceName;
    @Column(nullable = false)
    private Long nextValue;
    @Column(nullable = false)
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
