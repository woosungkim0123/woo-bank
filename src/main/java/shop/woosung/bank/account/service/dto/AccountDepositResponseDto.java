package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.transaction.domain.Transaction;

@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
public class AccountDepositResponseDto {
    private final Long id;
    private final Long fullNumber;
    private final TransactionDto transaction;

    public static AccountDepositResponseDto from(Account depositAccount, Transaction depositTransaction) {
        return AccountDepositResponseDto.builder()
                .id(depositAccount.getId())
                .fullNumber(depositAccount.getFullNumber())
                .transaction(new TransactionDto(depositTransaction))
                .build();
    }

    @Getter
    public static class TransactionDto {
        private final Long id;
        private final String type;
        private final String sender;
        private final String receiver;
        private final Long amount;
        @JsonIgnore
        private final Long depositAccountBalance;
        private final String tel;
        private final String createdAt;

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