package shop.woosung.bank.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.common.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AccountResDto {
    @Getter @Setter
    public static class AccountRegisterResDto {

        private Long id;
        private Long number;
        private Long balance;

        public AccountRegisterResDto(Long id, Long number, Long balance) {
            this.id = id;
            this.number = number;
            this.balance = balance;
        }
    }

    @Getter @Setter
    public static class AccountDepositResDto {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositResDto(AccountEntity accountEntity, Transaction transaction) {
            this.id = accountEntity.getId();
            this.number = accountEntity.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().toString();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountWithdrawResDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountWithdrawResDto(AccountEntity accountEntity, Transaction transaction) {
            this.id = accountEntity.getId();
            this.number = accountEntity.getNumber();
            this.balance = accountEntity.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().toString();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountTransferResDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountTransferResDto(AccountEntity accountEntity, Transaction transaction) {
            this.id = accountEntity.getId();
            this.number = accountEntity.getNumber();
            this.balance = accountEntity.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().toString();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Getter @Setter
    public static class AccountDetailResDto {
        private Long id;
        private Long number;
        private Long balance;
        private List<TransactionDto> transactions = new ArrayList<>();

        public AccountDetailResDto(AccountEntity accountEntity, List<Transaction> transactions) {
            this.id = accountEntity.getId();
            this.number = accountEntity.getNumber();
            this.balance = accountEntity.getBalance();
            this.transactions = transactions.stream()
                    .map(transaction -> new TransactionDto(transaction, accountEntity.getNumber()))
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

                if (transaction.getDepositAccountEntity() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccountEntity() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (transaction.getDepositAccountEntity().getNumber() == accountNumber.longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }
}
