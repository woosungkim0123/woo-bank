package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.transaction.domain.Transaction;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountTransferResponseDto {
    private Long id;
    private Long fullNumber;
    private Long balance;
    private TransactionDto transaction;

    public static AccountTransferResponseDto from(AccountTransferLockResponseDto accountTransferLockResponseDto, Transaction transaction) {
        return AccountTransferResponseDto.builder()
                .id(accountTransferLockResponseDto.getWithdrawAccountDto().getId())
                .fullNumber(accountTransferLockResponseDto.getWithdrawAccountDto().getFullNumber())
                .balance(accountTransferLockResponseDto.getWithdrawAccountDto().getBalance())
                .transaction(TransactionDto.from(transaction))
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class TransactionDto {
        private final Long id;
        private final String type;
        private final String sender;
        private final String receiver;
        private final Long amount;
        @JsonIgnore
        private final Long depositAccountBalance;
        private final String createdAt;

        public static TransactionDto from(Transaction transaction) {
            return TransactionDto.builder()
                    .id(transaction.getId())
                    .type(transaction.getType().toString())
                    .sender(transaction.getSender())
                    .receiver(transaction.getReceiver())
                    .amount(transaction.getAmount())
                    .depositAccountBalance(transaction.getDepositAccountBalance())
                    .createdAt(CustomDateUtil.toStringFormat(transaction.getCreatedAt()))
                    .build();
        }
    }
}
