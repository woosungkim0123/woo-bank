package shop.woosung.bank.account.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.user.domain.User;

@Getter
public class AccountRegister {
    private final Long typeNumber;
    private final Long newNumber;
    private final String password;
    private final Long balance;
    private final AccountType accountType;
    private final User user;

    @Builder
    public AccountRegister(Long typeNumber, Long newNumber, String password, Long balance, AccountType accountType, User user) {
        this.typeNumber = typeNumber;
        this.newNumber = newNumber;
        this.password = password;
        this.balance = balance;
        this.accountType = accountType;
        this.user = user;
    }
}
