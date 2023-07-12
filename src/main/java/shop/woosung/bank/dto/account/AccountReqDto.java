package shop.woosung.bank.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AccountReqDto {
    @Getter @Setter
    public static class AccountRegisterReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
    }
}
