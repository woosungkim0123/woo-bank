package shop.woosung.bank.transaction.infrastructure.entity;

import java.time.LocalDateTime;

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
import javax.persistence.Table;

import lombok.AccessLevel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;
import shop.woosung.bank.transaction.domain.TransactionEnum;

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
    private AccountEntity withdrawAccountEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AccountEntity depositAccountEntity;

    @Column(nullable = false)
    private Long amount;

    private Long withdrawAccountBalance;

    private Long depositAccountBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionEnum gubun;

    private String sender;
    private String receiver;
    private String tel;

    @Builder
    public TransactionEntity(Long id, AccountEntity withdrawAccountEntity, AccountEntity depositAccountEntity, Long amount,
                             Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum gubun, String sender,
                             String receiver, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.withdrawAccountEntity = withdrawAccountEntity;
        this.depositAccountEntity = depositAccountEntity;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.gubun = gubun;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}