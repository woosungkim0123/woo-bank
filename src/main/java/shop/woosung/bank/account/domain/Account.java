package shop.woosung.bank.account.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.handler.exception.NotAccountOwnerException;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class Account {

    private Long id;
    private Long number;
    private Long fullnumber;
    private String password;
    private Long balance;
    private AccountType type;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long id, Long number, Long fullnumber, String password, Long balance, AccountType type, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.fullnumber = fullnumber;
        this.password = password;
        this.balance = balance;
        this.type = type;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account register(AccountRegister accountRegister, PasswordEncoder passwordEncoder) {
        return Account.builder()
                .number(accountRegister.getNewNumber())
                .fullnumber(concatNumbers(accountRegister.getTypeNumber(), accountRegister.getNewNumber()))
                .password(passwordEncoder.encode(accountRegister.getPassword()))
                .balance(accountRegister.getBalance())
                .type(accountRegister.getAccountType())
                .user(accountRegister.getUser())
                .build();
    }

    public void checkOwner(Long userId) {
        if(!user.getId().equals(userId)) {
            throw new NotAccountOwnerException();
        }
    }

    public void deposit(Long amount) {
        this.balance = this.balance + amount;
    }
//
//    public void checkSamePassword(Long password) {
//        if(!this.password.equals(password)) {
//            throw new CustomApiException("비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//    public void checkBalance(Long amount) {
//        if(this.balance < amount) {
//            throw new CustomApiException("계좌 잔액이 부족합니다.");
//        }
//    }

    public void withdraw(Long amount) {
        balance = balance - amount;
    }

    private static Long concatNumbers(Long num1, Long num2) {
        String concatenatedStr = num1 + "" + num2;
        return Long.parseLong(concatenatedStr);
    }
}
