package shop.woosung.bank.account.util;

import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.domain.AccountRegister;
import shop.woosung.bank.user.domain.User;


public class AccountServiceToDomainConverter {

    static public AccountRegister accountRegisterConvert(AccountRegisterRequestDto accountRegisterRequestDto, Long typeNumber, Long newNumber, User user) {
        return AccountRegister.builder()
                .typeNumber(typeNumber)
                .newNumber(newNumber)
                .password(accountRegisterRequestDto.getPassword())
                .balance(accountRegisterRequestDto.getBalance())
                .accountType(accountRegisterRequestDto.getType())
                .user(user)
                .build();
    }
}
