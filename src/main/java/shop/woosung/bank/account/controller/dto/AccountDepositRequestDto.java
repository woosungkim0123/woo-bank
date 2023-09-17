package shop.woosung.bank.account.controller.dto;

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

    @Pattern(regexp = "^(DEPOSIT|WITHDRAW)$")
    @NotEmpty
    private TransactionType transactionType;

    @Pattern(regexp = "^[0-9]{11}")
    @NotEmpty
    private String tel;
}
