package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;


@Getter
public class AccountTransferRequestServiceDto {
    private final Long withdrawFullNumber;
    private final Long depositFullNumber;
    private final Long withdrawPassword;
    private final Long amount;
    private final TransactionType transactionType;

    @Builder
    public AccountTransferRequestServiceDto(Long withdrawFullNumber, Long depositFullNumber, Long withdrawPassword, Long amount, TransactionType transactionType) {
        this.withdrawFullNumber = withdrawFullNumber;
        this.depositFullNumber = depositFullNumber;
        this.withdrawPassword = withdrawPassword;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
