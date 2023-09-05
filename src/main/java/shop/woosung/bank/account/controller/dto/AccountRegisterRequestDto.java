package shop.woosung.bank.account.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.AccountType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRegisterRequestDto {

    @Size(min = 4, max = 4)
    @NotEmpty
    private String password;

    private long balance;

    @NotNull(message = "계좌 종류를 선택해주세요")
    private AccountType type;

    @Builder
    public AccountRegisterRequestDto(String password, long balance, AccountType type) {
        this.password = password;
        this.balance = balance;
        this.type = type;
    }
}
