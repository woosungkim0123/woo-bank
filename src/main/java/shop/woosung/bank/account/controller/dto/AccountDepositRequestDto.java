package shop.woosung.bank.account.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.transaction.domain.TransactionType;

import javax.validation.constraints.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccountDepositRequestDto {
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private Long fullNumber;

    @Positive
    @NotNull
    private Long amount;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private String sender;

    @Pattern(regexp = "^[0-9]{11}")
    @NotEmpty
    private String tel;

    @Builder
    public AccountDepositRequestDto(Long fullNumber, Long amount, TransactionType transactionType, String sender, String tel) {
        this.fullNumber = fullNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.sender = sender;
        this.tel = tel;
    }
}
