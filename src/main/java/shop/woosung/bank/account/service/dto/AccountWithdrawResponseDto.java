package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.transaction.domain.Transaction;

@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
public class AccountWithdrawResponseDto {
    private final Long id;
    private final Long fullNumber;
    private final Long balance;
    private final TransactionDto transaction;

    public static AccountWithdrawResponseDto from(Account withdrawAccount, Transaction depositTransaction) {
        return AccountWithdrawResponseDto.builder()
                .id(withdrawAccount.getId())
                .fullNumber(withdrawAccount.getFullNumber())
                .balance(withdrawAccount.getBalance())
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
        private final String createdAt;

        public TransactionDto(Transaction transaction) {
            this.id = transaction.getId();
            this.type = transaction.getType().name();
            this.sender = transaction.getSender();
            this.receiver = transaction.getReceiver();
            this.amount = transaction.getAmount();
            this.depositAccountBalance = transaction.getDepositAccountBalance();
            this.createdAt = transaction.getCreatedAt().toString();
        }
    }
}