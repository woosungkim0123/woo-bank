package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.account.AccountResDto;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;

@Getter
public class AccountDepositResponseDto {
    private final Long id;
    private final Long number;
    private final TransactionDto transaction;

    public AccountDepositResponseDto(Account depositAccount, Transaction depositTransaction) {
        this.id = depositAccount.getId();
        this.number = depositAccount.getNumber();
        this.transaction = new TransactionDto(depositTransaction);
    }

    @Getter @Setter
    public class TransactionDto {
        private Long id;
        private String type;
        private String sender;
        private String receiver;
        private Long amount;
        @JsonIgnore
        private Long depositAccountBalance;
        private String tel;
        private String createdAt;

        public TransactionDto(Transaction transaction) {
            this.id = transaction.getId();
            this.type = transaction.getType().name();
            this.sender = transaction.getSender();
            this.receiver = transaction.getReceiver();
            this.amount = transaction.getAmount();
            this.depositAccountBalance = transaction.getDepositAccountBalance();
            this.tel = transaction.getTel();
            this.createdAt = transaction.getCreatedAt().toString();
        }
    }
}