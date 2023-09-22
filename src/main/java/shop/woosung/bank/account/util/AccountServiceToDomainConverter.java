package shop.woosung.bank.account.util;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountRegister;
import shop.woosung.bank.transaction.domain.DepositTransactionCreate;
import shop.woosung.bank.account.service.dto.AccountDepositRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.user.domain.User;

public class AccountServiceToDomainConverter {

    static public AccountRegister accountRegisterConvert(AccountRegisterRequestServiceDto accountRegisterRequestServiceDto, Long typeNumber, Long newNumber, User user) {
        return AccountRegister.builder()
                .typeNumber(typeNumber)
                .newNumber(newNumber)
                .password(accountRegisterRequestServiceDto.getPassword())
                .balance(accountRegisterRequestServiceDto.getBalance())
                .accountType(accountRegisterRequestServiceDto.getType())
                .user(user)
                .build();
    }

    static public DepositTransactionCreate depositTransactionCreateConvert(AccountDepositRequestServiceDto accountDepositRequestServiceDto, Account depositAccount) {
        return DepositTransactionCreate.builder()
                .depositAccount(depositAccount)
                .depositAccountBalance(depositAccount.getBalance())
                .amount(accountDepositRequestServiceDto.getAmount())
                .type(accountDepositRequestServiceDto.getTransactionType())
                .sender(accountDepositRequestServiceDto.getSender())
                .receiver(depositAccount.getFullNumber().toString())
                .tel(accountDepositRequestServiceDto.getTel())
                .build();
    }
}