package shop.woosung.bank.account.controller.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

import javax.validation.constraints.*;

@Getter
public class AccountWithdrawRequestDto {
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private final Long fullNumber;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private final Long password;

    @Positive
    @NotNull
    private final Long amount;

    @NotNull
    private final TransactionType transactionType;

    @Builder
    public AccountWithdrawRequestDto(Long fullNumber, Long password, Long amount, TransactionType transactionType) {
        this.fullNumber = fullNumber;
        this.password = password;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
