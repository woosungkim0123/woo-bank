package shop.woosung.bank.account.util;

import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.user.controller.dto.JoinRequestDto;
import shop.woosung.bank.user.service.dto.JoinRequestServiceDto;

public class AccountControllerToServiceConverter {

    public static AccountRegisterRequestServiceDto accountRegisterRequestConvert(AccountRegisterRequestDto accountRegisterRequestDto){
        return AccountRegisterRequestServiceDto.builder()
                .password(accountRegisterRequestDto.getPassword())
                .balance(accountRegisterRequestDto.getBalance())
                .type(accountRegisterRequestDto.getType())
                .build();
    }
}
