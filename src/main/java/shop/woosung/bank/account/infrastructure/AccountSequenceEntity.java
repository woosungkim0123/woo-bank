package shop.woosung.bank.account.infrastructure;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountSequenceEntity {

    @Id
    private String sequenceName;
    private Long nextValue;
    private Long incrementBy;

    @Builder
    public AccountSequenceEntity(String sequenceName, Long nextValue, Long incrementBy) {
        this.sequenceName = sequenceName;
        this.nextValue = nextValue;
        this.incrementBy = incrementBy;
    }

    public void incrementNextValue() {
        this.nextValue += this.incrementBy;
    }
}
