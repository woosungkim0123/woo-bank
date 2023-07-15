package shop.woosung.bank.dto.account;

import lombok.Getter;
import lombok.Setter;
import shop.woosung.bank.domain.account.Account;


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
}
