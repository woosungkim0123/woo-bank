package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

@Getter
public class AccountWithdrawRequestServiceDto {
    private final Long fullNumber;
    private final Long password;
    private final Long amount;
    private final TransactionType transactionType;

    @Builder
    public AccountWithdrawRequestServiceDto(Long fullNumber, Long password, Long amount, TransactionType transactionType) {
        this.fullNumber = fullNumber;
        this.password = password;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
