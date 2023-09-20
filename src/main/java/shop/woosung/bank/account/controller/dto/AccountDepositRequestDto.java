package shop.woosung.bank.account.controller.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

import javax.validation.constraints.*;

@Getter
public class AccountDepositRequestDto {
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private Long fullnumber;

    @Positive
    @NotNull
    private Long amount;

    @NotNull
    private TransactionType transactionType;

    @Pattern(regexp = "^[0-9]{11}")
    @NotEmpty
    private String tel;

    @Builder
    public AccountDepositRequestDto(Long fullnumber, Long amount, TransactionType transactionType, String tel) {
        this.fullnumber = fullnumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.tel = tel;
    }
}
