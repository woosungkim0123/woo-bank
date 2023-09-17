package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.user.domain.User;

@Getter
public class AccountDepositRequestServiceDto {
    private final Long fullnumber;
    private final Long amount;
    private final TransactionType transactionType;
    private final String tel;
    private final User user;

    @Builder
    public AccountDepositRequestServiceDto(Long fullnumber, Long amount, TransactionType transactionType, String tel, User user) {
        this.fullnumber = fullnumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.tel = tel;
        this.user = user;
    }
}
