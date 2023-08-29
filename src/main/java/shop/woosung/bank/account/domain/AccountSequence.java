package shop.woosung.bank.account.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountSequence {
    private AccountType sequenceName;
    private Long nextValue;
    private Long incrementBy;

    @Builder
    public AccountSequence(AccountType sequenceName, Long nextValue, Long incrementBy) {
        this.sequenceName = sequenceName;
        this.nextValue = nextValue;
        this.incrementBy = incrementBy;
    }

    public void incrementNextValue() {
        this.nextValue += this.incrementBy;
    }
}
