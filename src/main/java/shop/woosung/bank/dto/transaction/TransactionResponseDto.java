package shop.woosung.bank.dto.transaction;

import lombok.Getter;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.common.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionResponseDto {

    @Getter
    public static class TransactionResponseListDto {
        private List<TransactionDto> transactions = new ArrayList<>();

        public TransactionResponseListDto(Account account, List<Transaction> transactions) {
            this.transactions = transactions.stream()
                    .map(transaction -> new TransactionDto(transaction, account.getNumber()))
                    .collect(Collectors.toList());
        }

        @Getter
        public class TransactionDto {
            private Long id;
            private String type;
            private Long amount;
            private String sender;
            private String receiver;
            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.type = transaction.getGubun().getValue();
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
                    if (transaction.getDepositAccount().getNumber() == accountNumber.longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }

    }
}
