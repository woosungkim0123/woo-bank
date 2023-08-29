package shop.woosung.bank.account.controller.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


@Getter

public class AccountRegisterRequestDto {
    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long password;

    private String type;

    @Builder
    public AccountRegisterRequestDto(Long password) {
        this.password = password;
    }
}
