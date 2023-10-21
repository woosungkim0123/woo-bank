package shop.woosung.bank.account.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountDto {
    private Long id;
    private Long number;
    private Long fullNumber;
    private String password;
    private Long balance;
    private AccountType type;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .number(account.getNumber())
                .fullNumber(account.getFullNumber())
                .password(account.getPassword())
                .balance(account.getBalance())
                .type(account.getType())
                .user(account.getUser())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
