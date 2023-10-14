package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
public class AccountTransferLockServiceDto {
    private final Long withdrawFullNumber;
    private final Long depositFullNumber;
    private final Long withdrawPassword;
    private final Long amount;

    @Builder
    public AccountTransferLockServiceDto(Long withdrawFullNumber, Long depositFullNumber, Long withdrawPassword, Long amount, User user) {
        this.withdrawFullNumber = withdrawFullNumber;
        this.depositFullNumber = depositFullNumber;
        this.withdrawPassword = withdrawPassword;
        this.amount = amount;
    }
}
