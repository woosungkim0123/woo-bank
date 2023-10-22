package shop.woosung.bank.transaction.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

import java.time.LocalDateTime;

@Getter
public class Transaction {
    private final Long id;
    private final Account withdrawAccount;
    private final Account depositAccount;
    private final Long amount;
    private final Long withdrawAccountBalance;
    private final Long depositAccountBalance;
    private final TransactionType type;
    private final String sender;
    private final String receiver;
    private final String tel;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

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

    public static Transaction createDepositTransaction(DepositTransactionCreate depositTransactionCreate) {
        return Transaction.builder()
                .depositAccount(depositTransactionCreate.getDepositAccount())
                .depositAccountBalance(depositTransactionCreate.getDepositAccountBalance())
                .amount(depositTransactionCreate.getAmount())
                .type(depositTransactionCreate.getType())
                .sender(depositTransactionCreate.getSender())
                .receiver(depositTransactionCreate.getReceiver())
                .tel(depositTransactionCreate.getTel())
                .build();
    }

    public static Transaction createWithdrawTransaction(WithdrawTransactionCreate withdrawTransactionCreate) {
        return Transaction.builder()
                .withdrawAccount(withdrawTransactionCreate.getWithdrawAccount())
                .withdrawAccountBalance(withdrawTransactionCreate.getWithdrawAccountBalance())
                .amount(withdrawTransactionCreate.getAmount())
                .type(withdrawTransactionCreate.getType())
                .sender(withdrawTransactionCreate.getSender())
                .receiver(withdrawTransactionCreate.getReceiver())
                .build();
    }
}
