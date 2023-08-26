package shop.woosung.bank.account.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

import java.util.stream.Collectors;


@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountRegisterResponseDto {

    private final Long id;
    private final Long number;
    private final Long balance;

    public static AccountRegisterResponseDto from(Account account) {
        return AccountRegisterResponseDto.builder()
                .id(account.getId())
                .number(account.getNumber())
                .balance(account.getBalance())
                .build();
    }

}
