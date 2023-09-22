package shop.woosung.bank.transaction.service.dto;

import lombok.Getter;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.common.util.CustomDateUtil;
import shop.woosung.bank.transaction.domain.Transaction;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TransactionResponseListDto {
    private final List<TransactionDto> transactions;

    public TransactionResponseListDto(Account account, List<Transaction> transactions) {
        this.transactions = transactions.stream()
                .map(transaction -> new TransactionDto(transaction, account.getFullNumber()))
                .collect(Collectors.toList());
    }

    @Getter
    public class TransactionDto {
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
            } else if (transaction.getWithdrawAccount() == null) {
                this.balance = transaction.getDepositAccountBalance();
            } else {
                if (transaction.getDepositAccount().getNumber() == accountFullNumber.longValue()) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    this.balance = transaction.getWithdrawAccountBalance();
                }
            }
        }
    }

}