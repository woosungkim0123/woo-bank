package shop.woosung.bank.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

public class AccountReqDto {
    @Getter @Setter
    public static class AccountRegisterReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
    }

    @Getter @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 20)
        private Long number;
        @Positive
        @NotNull
        private Long amount;
        @Pattern(regexp = "^(DEPOSIT|WITHDRAW)$")
        @NotEmpty
        private String type;
        @Pattern(regexp = "^[0-9]{11}")
        @NotEmpty
        private String tel;
    }

    @Getter @Setter
    public static class AccountWithdrawReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 20)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
        @Positive
        @NotNull
        private Long amount;
        @Pattern(regexp = "^(DEPOSIT|WITHDRAW)$")
        @NotEmpty
        private String type;
    }

    @Getter @Setter
    public static class AccountTransferReqDto {
        @NotNull
        @Digits(integer = 20, fraction = 20)
        private Long withdrawNumber;
        @NotNull
        @Digits(integer = 20, fraction = 20)
        private Long depositNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;
        @Positive
        @NotNull
        private Long amount;
        @Pattern(regexp = "TRANSFER")
        @NotEmpty
        private String type;
    }
}
