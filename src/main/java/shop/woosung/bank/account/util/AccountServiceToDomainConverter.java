package shop.woosung.bank.account.util;

import shop.woosung.bank.account.domain.AccountRegister;
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
}
