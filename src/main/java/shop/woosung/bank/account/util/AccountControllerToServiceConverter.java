package shop.woosung.bank.account.util;

import shop.woosung.bank.account.controller.dto.AccountDepositRequestDto;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.dto.AccountTransferRequestDto;
import shop.woosung.bank.account.controller.dto.AccountWithdrawRequestDto;
import shop.woosung.bank.account.service.dto.AccountDepositRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountTransferRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawRequestServiceDto;

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
                .fullNumber(accountDepositRequestDto.getFullNumber())
                .amount(accountDepositRequestDto.getAmount())
                .transactionType(accountDepositRequestDto.getTransactionType())
                .sender(accountDepositRequestDto.getSender())
                .tel(accountDepositRequestDto.getTel())
                .build();
    }

    public static AccountWithdrawRequestServiceDto accountWithdrawRequestConvert(AccountWithdrawRequestDto accountWithdrawRequestDto){
        return AccountWithdrawRequestServiceDto.builder()
                .fullNumber(accountWithdrawRequestDto.getFullNumber())
                .password(accountWithdrawRequestDto.getPassword())
                .amount(accountWithdrawRequestDto.getAmount())
                .transactionType(accountWithdrawRequestDto.getTransactionType())
                .build();
    }

    public static AccountTransferRequestServiceDto accountTransferRequestConvert(AccountTransferRequestDto accountTransferRequestDto){
        return AccountTransferRequestServiceDto.builder()
                .withdrawFullNumber(accountTransferRequestDto.getWithdrawFullNumber())
                .depositFullNumber(accountTransferRequestDto.getDepositFullNumber())
                .withdrawPassword(accountTransferRequestDto.getWithdrawPassword())
                .amount(accountTransferRequestDto.getAmount())
                .transactionType(accountTransferRequestDto.getTransactionType())
                .build();
    }
}
