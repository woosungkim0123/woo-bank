package shop.woosung.bank.account.controller.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

import javax.validation.constraints.*;

@Getter
public class AccountDepositRequestDto {
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private final Long fullNumber;

    @Positive
    @NotNull
    private final Long amount;

    @NotNull
    private final TransactionType transactionType;

    @NotNull
    private final String sender;

    @Pattern(regexp = "^[0-9]{11}")
    @NotEmpty
    private final String tel;

    @Builder
    public AccountDepositRequestDto(Long fullNumber, Long amount, TransactionType transactionType, String sender, String tel) {
        this.fullNumber = fullNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.sender = sender;
        this.tel = tel;
    }
}
