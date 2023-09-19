package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;

@Getter
public class AccountDepositRequestServiceDto {
    private final Long fullnumber;
    private final Long amount;
    private final TransactionType transactionType;
    private final String tel;

    @Builder
    public AccountDepositRequestServiceDto(Long fullnumber, Long amount, TransactionType transactionType, String tel) {
        this.fullnumber = fullnumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.tel = tel;
    }
}
