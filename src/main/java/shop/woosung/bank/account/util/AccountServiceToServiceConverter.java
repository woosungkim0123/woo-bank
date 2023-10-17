package shop.woosung.bank.account.util;

import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountTransferRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawRequestServiceDto;
import shop.woosung.bank.user.domain.User;

public class AccountServiceToServiceConverter {

    public static AccountWithdrawLockServiceDto accountWithdrawLockServiceDtoConvert(AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto, User user){
        return AccountWithdrawLockServiceDto.builder()
                .user(user)
                .fullNumber(accountWithdrawRequestServiceDto.getFullNumber())
                .password(accountWithdrawRequestServiceDto.getPassword())
                .amount(accountWithdrawRequestServiceDto.getAmount())
                .build();
    }

    public static AccountTransferLockServiceDto accountTransferLockServiceDtoConvert(AccountTransferRequestServiceDto accountTransferRequestServiceDto, User user){
        return AccountTransferLockServiceDto.builder()
                .withdrawFullNumber(accountTransferRequestServiceDto.getWithdrawFullNumber())
                .withdrawPassword(accountTransferRequestServiceDto.getWithdrawPassword())
                .depositFullNumber(accountTransferRequestServiceDto.getDepositFullNumber())
                .amount(accountTransferRequestServiceDto.getAmount())
                .user(user)
                .build();
    }
}
