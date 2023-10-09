package shop.woosung.bank.account.controller.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

import javax.validation.constraints.*;

@Getter
public class AccountTransferRequestDto {
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private final Long withdrawFullNumber;

    @NotNull
    @Digits(integer = 19, fraction = 0)
    private final Long depositFullNumber;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private final Long withdrawPassword;

    @Positive
    @NotNull
    private final Long amount;

    @NotNull
    private final TransactionType transactionType;

    @AssertTrue(message = "계좌 전송 타입 오류")
    public boolean isTransferOnly() {
        return transactionType == TransactionType.TRANSFER;
    }

    @Builder
    public AccountTransferRequestDto(Long withdrawFullNumber, Long depositFullNumber, Long withdrawPassword, Long amount, TransactionType transactionType) {
        this.withdrawFullNumber = withdrawFullNumber;
        this.depositFullNumber = depositFullNumber;
        this.withdrawPassword = withdrawPassword;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
