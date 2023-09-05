package shop.woosung.bank.account.domain;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AccountTypeNumber {
    private AccountType accountType;
    private Long number;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public AccountTypeNumber(AccountType accountType, Long number, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.accountType = accountType;
        this.number = number;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

