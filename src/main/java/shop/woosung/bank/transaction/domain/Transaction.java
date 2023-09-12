package shop.woosung.bank.transaction.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

import java.time.LocalDateTime;

@Getter
public class Transaction {

    private Long id;
    private Account withdrawAccount;
    private Account depositAccount;
    private Long amount;
    private Long withdrawAccountBalance;
    private Long depositAccountBalance;
    private TransactionType type;
    private String sender;
    private String receiver;
    private String tel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance, Long depositAccountBalance, TransactionType type, String sender, String receiver, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
