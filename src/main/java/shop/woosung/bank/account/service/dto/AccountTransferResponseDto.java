package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.transaction.domain.Transaction;

@Builder(access = AccessLevel.PRIVATE)
public class AccountTransferResponseDto {
    private Long id;
    private Long fullNumber;
    private Long balance;
    private TransactionDto transaction;

    public AccountTransferResponseDto from(Account account, Transaction transaction) {
        return AccountTransferResponseDto.builder()
                .id(account.getId())
                .fullNumber(account.getFullNumber())
                .balance(account.getBalance())
                .transaction(new TransactionDto(transaction))
                .build();
    }

    public class TransactionDto {
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
            this.type = transaction.getType().toString();
            this.sender = transaction.getSender();
            this.receiver = transaction.getReceiver();
            this.amount = transaction.getAmount();
            this.depositAccountBalance = transaction.getDepositAccountBalance();
            this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
        }
    }
}
