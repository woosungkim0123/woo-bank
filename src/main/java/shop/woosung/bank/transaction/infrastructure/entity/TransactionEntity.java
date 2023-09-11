package shop.woosung.bank.transaction.infrastructure.entity;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class TransactionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity withdrawAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity depositAccount;

    @Column(nullable = false)
    private Long amount;

    private Long withdrawAccountBalance;

    private Long depositAccountBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String sender;

    private String receiver;

    private String tel;

    public static TransactionEntity fromModel(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.id = transaction.getId();
        transactionEntity.withdrawAccount = AccountEntity.fromModel(transaction.getWithdrawAccount());
        transactionEntity.depositAccount = AccountEntity.fromModel(transaction.getDepositAccount());
        transactionEntity.amount = transaction.getAmount();
        transactionEntity.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
        transactionEntity.depositAccountBalance = transaction.getDepositAccountBalance();
        transactionEntity.type = transaction.getType();
        transactionEntity.sender = transaction.getSender();
        transactionEntity.receiver = transaction.getReceiver();
        transactionEntity.tel = transaction.getTel();
        return transactionEntity;
    }

    public Transaction toModel() {
        return Transaction.builder()
                .id(id)
                .withdrawAccount(withdrawAccount.toModel())
                .depositAccount(depositAccount.toModel())
                .amount(amount)
                .withdrawAccountBalance(withdrawAccountBalance)
                .depositAccountBalance(depositAccountBalance)
                .type(type)
                .sender(sender)
                .receiver(receiver)
                .tel(tel)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}