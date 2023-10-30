package shop.woosung.bank.transaction.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

@Getter
public class WithdrawTransactionCreate {
    private final Account withdrawAccount;
    private final Long withdrawAccountBalance;
    private final Long amount;
    private final TransactionType type;
    private final String sender;
    private final String receiver;

    @Builder
    public WithdrawTransactionCreate(Account withdrawAccount, Long withdrawAccountBalance, Long amount, TransactionType type, String sender, String receiver) {
        this.withdrawAccount = withdrawAccount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.amount = amount;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
    }
}
