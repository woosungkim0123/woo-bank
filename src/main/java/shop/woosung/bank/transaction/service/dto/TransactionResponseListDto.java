package shop.woosung.bank.transaction.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.service.dto.AccountDto;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.transaction.domain.Transaction;

import java.util.List;
import java.util.stream.Collectors;


@Builder(access = AccessLevel.PRIVATE)
@Getter
public class TransactionResponseListDto {
    private List<TransactionDto> transactions;

    public static TransactionResponseListDto from(AccountDto accountDto, List<Transaction> transactions) {
        return TransactionResponseListDto.builder()
                .transactions(transactions.stream()
                        .map(transaction -> new TransactionDto(transaction, accountDto.getFullNumber()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    public static class TransactionDto {
        private final Long id;
        private final String type;
        private final Long amount;
        private final String sender;
        private final String receiver;
        private final String tel;
        private final String createdAt;
        private final Long balance;

        public TransactionDto(Transaction transaction, Long accountFullNumber) {
            this.id = transaction.getId();
            this.type = transaction.getType().getValue();
            this.amount = transaction.getAmount();
            this.sender = transaction.getSender();
            this.receiver = transaction.getReceiver();
            this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();
            this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());

            if (transaction.getDepositAccount() == null) {
                this.balance = transaction.getWithdrawAccountBalance();
            } else if (transaction.getWithdrawAccount() == null || transaction.getDepositAccount().getFullNumber() == accountFullNumber.longValue()) {
                this.balance = transaction.getDepositAccountBalance();
            } else {
                this.balance = transaction.getWithdrawAccountBalance();
            }
        }
    }
}