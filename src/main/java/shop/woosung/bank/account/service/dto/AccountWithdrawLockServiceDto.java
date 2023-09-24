package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
public class AccountWithdrawLockServiceDto {
    private final User user;
    private final Long fullNumber;
    private final Long password;
    private final Long amount;

    @Builder
    public AccountWithdrawLockServiceDto(User user, Long fullNumber, Long password, Long amount) {
        this.user = user;
        this.fullNumber = fullNumber;
        this.password = password;
        this.amount = amount;
    }
}
