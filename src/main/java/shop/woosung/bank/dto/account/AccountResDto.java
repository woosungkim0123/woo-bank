package shop.woosung.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.util.CustomDateUtil;

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

    @Getter
    public static class AccountListResDto {
        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListResDto(User user, List<Account> accounts) {
            this.fullname = user.getFullname();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }
        /*
            json이 모든 필드를 getter해서 원하지 않는 LazyLoading이 일어날 수도 있어서 dto사용
         */
        @Getter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Getter @Setter
    public static class AccountDepositResDto {
        private Long id;
        private Long number;
        private TransactionDto transaction;

        public AccountDepositResDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
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

}
