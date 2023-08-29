package shop.woosung.bank.account.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woosung.bank.account.domain.AccountType;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRegisterRequestDto {
    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long password;

    @NotNull(message = "계좌 종류를 확인해주세요.")
    private AccountType type;

    @Builder
    public AccountRegisterRequestDto(Long password, AccountType type) {
        this.password = password;
        this.type = type;
    }
}
