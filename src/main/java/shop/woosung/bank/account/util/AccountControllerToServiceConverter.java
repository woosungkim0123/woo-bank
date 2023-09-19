package shop.woosung.bank.account.util;

import shop.woosung.bank.account.controller.dto.AccountDepositRequestDto;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.service.dto.AccountDepositRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;

public class AccountControllerToServiceConverter {

    public static AccountRegisterRequestServiceDto accountRegisterRequestConvert(AccountRegisterRequestDto accountRegisterRequestDto){
        return AccountRegisterRequestServiceDto.builder()
                .password(accountRegisterRequestDto.getPassword())
                .balance(accountRegisterRequestDto.getBalance())
                .type(accountRegisterRequestDto.getType())
                .build();
    }

    public static AccountDepositRequestServiceDto accountDepositRequestConvert(AccountDepositRequestDto accountDepositRequestDto){
        return AccountDepositRequestServiceDto.builder()
                .fullnumber(accountDepositRequestDto.getFullnumber())
                .amount(accountDepositRequestDto.getAmount())
                .transactionType(accountDepositRequestDto.getTransactionType())
                .tel(accountDepositRequestDto.getTel())
                .build();
    }
}
