package shop.woosung.bank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.user.User;

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

}
