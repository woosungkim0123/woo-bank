package shop.woosung.bank.transaction.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

@Getter
public class DepositTransactionCreate {
    private final Account depositAccount;
    private final Long depositAccountBalance;
    private final Long amount;
    private final TransactionType type;
    private final String sender;
    private final String receiver;
    private final String tel;

    @Builder
    public DepositTransactionCreate(Account depositAccount, Long depositAccountBalance, Long amount, TransactionType type, String sender, String receiver, String tel) {
        this.depositAccount = depositAccount;
        this.depositAccountBalance = depositAccountBalance;
        this.amount = amount;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
    }
}
