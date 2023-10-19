package shop.woosung.bank.account.util;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountRegister;
import shop.woosung.bank.account.service.dto.AccountWithdrawRequestServiceDto;
import shop.woosung.bank.transaction.domain.DepositTransactionCreate;
import shop.woosung.bank.account.service.dto.AccountDepositRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.transaction.domain.WithdrawTransactionCreate;
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

    // TODO 여기있는게 맞을까?
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

    // TODO 여기있는게 맞을까?
    static public WithdrawTransactionCreate withdrawTransactionCreateConvert(AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto, Account withdrawAccount, String receiver) {
        return WithdrawTransactionCreate.builder()
                .withdrawAccount(withdrawAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .amount(accountWithdrawRequestServiceDto.getAmount())
                .type(accountWithdrawRequestServiceDto.getTransactionType())
                .sender(accountWithdrawRequestServiceDto.getFullNumber().toString())
                .receiver(receiver)
                .build();
    }
}