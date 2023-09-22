package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

@Getter
public class AccountDepositRequestServiceDto {
    private final Long fullNumber;
    private final Long amount;
    private final TransactionType transactionType;
    private final String sender;
    private final String tel;

    @Builder
    public AccountDepositRequestServiceDto(Long fullNumber, Long amount, TransactionType transactionType, String sender, String tel) {
        this.fullNumber = fullNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.sender = sender;
        this.tel = tel;
    }
}
