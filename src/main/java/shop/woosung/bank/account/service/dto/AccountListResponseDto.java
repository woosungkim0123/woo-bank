package shop.woosung.bank.account.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountListResponseDto {

    private String username;
    private List<AccountDto> accounts;

    public static AccountListResponseDto from(User user, List<Account> accounts) {
        return AccountListResponseDto.builder()
                .username(user.getName())
                .accounts(accounts.stream()
                        .map(AccountDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    public static class AccountDto {
        private final Long id;
        private final Long number;
        private final Long balance;

        public AccountDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
