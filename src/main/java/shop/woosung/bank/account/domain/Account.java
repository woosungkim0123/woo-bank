package shop.woosung.bank.account.domain;

import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.common.infrastructure.BaseTimeEntity;
import shop.woosung.bank.handler.ex.CustomApiException;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class Account extends BaseTimeEntity {

    private Long id;
    private Long number;
    private Long password;
    private Long balance;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long id, Long number, Long password, Long balance, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void checkOwner(Long userId) {
        if(!user.getId().equals(userId)) {
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void deposit(Long amount) {
        this.balance = this.balance + amount;
    }

    public void checkSamePassword(Long password) {
        if(!this.password.equals(password)) {
            throw new CustomApiException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void checkBalance(Long amount) {
        if(this.balance < amount) {
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
    }

    public void withdraw(Long amount) {
        balance = balance - amount;
    }
}
