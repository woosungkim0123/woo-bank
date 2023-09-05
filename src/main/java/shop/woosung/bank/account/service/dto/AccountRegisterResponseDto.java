package shop.woosung.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountRegisterResponseDto {

    private final Long id;
    @JsonIgnore
    private final Long number;
    private final Long fullnumber;
    private final Long balance;

    public static AccountRegisterResponseDto from(Account account) {
        return AccountRegisterResponseDto.builder()
                .id(account.getId())
                .number(account.getNumber())
                .fullnumber(account.getFullnumber())
                .balance(account.getBalance())
                .build();
    }

}
