package shop.woosung.bank.account.service.dto;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.AccountType;

@Getter
public class AccountRegisterRequestServiceDto {
    private String password;
    private long balance;
    private AccountType type;

    @Builder
    public AccountRegisterRequestServiceDto(String password, long balance, AccountType type) {
        this.password = password;
        this.balance = balance;
        this.type = type;
    }
}

